package source

internal val GloopShaderSource = """
    uniform float iTime;
    uniform float density;
    uniform float2 seed;
    uniform float speed;
    uniform float waveScale;
    uniform float scale;
    uniform float lineWeight;
    layout(color) uniform half4 bgColor;
    layout(color) uniform half4 lineColor;

    const float INV_289 = 1.0 / 289.0;

    float3 mod289v3(float3 x) { return x - floor(x * INV_289) * 289.0; }
    float4 mod289v4(float4 x) { return x - floor(x * INV_289) * 289.0; }
    float4 permute4(float4 x) { return mod289v4(((x * 34.0) + 10.0) * x); }
    float4 taylorInvSqrt(float4 r) {
        return 1.79284291400159 - 0.85373472095314 * r;
    }

    float simplex3(float3 v) {
        const float2 C = float2(1.0 / 6.0, 1.0 / 3.0);
        const float4 D = float4(0.0, 0.5, 1.0, 2.0);

        float3 i = floor(v + dot(v, float3(C.y)));
        float3 x0 = v - i + dot(i, float3(C.x));

        float3 g = step(x0.yzx, x0.xyz);
        float3 l = 1.0 - g;
        float3 i1 = min(g.xyz, l.zxy);
        float3 i2 = max(g.xyz, l.zxy);

        float3 x1 = x0 - i1 + float3(C.x);
        float3 x2 = x0 - i2 + float3(C.y);
        float3 x3 = x0 - float3(D.y);

        i = mod289v3(i);
        float4 p = permute4(permute4(permute4(
            i.z + float4(0.0, i1.z, i2.z, 1.0))
            + i.y + float4(0.0, i1.y, i2.y, 1.0))
            + i.x + float4(0.0, i1.x, i2.x, 1.0));

        float3 ns = 0.142857142857 * D.wyz - D.xzx;

        float4 j = p - 49.0 * floor(p * ns.z * ns.z);
        float4 x_ = floor(j * ns.z);
        float4 y_ = floor(j - 7.0 * x_);

        float4 x = x_ * ns.x + ns.yyyy;
        float4 y = y_ * ns.x + ns.yyyy;
        float4 h = 1.0 - abs(x) - abs(y);

        float4 b0 = float4(x.xy, y.xy);
        float4 b1 = float4(x.zw, y.zw);

        float4 s0 = floor(b0) * 2.0 + 1.0;
        float4 s1 = floor(b1) * 2.0 + 1.0;
        float4 sh = -step(h, float4(0.0));

        float4 a0 = b0.xzyw + s0.xzyw * sh.xxyy;
        float4 a1 = b1.xzyw + s1.xzyw * sh.zzww;

        float3 p0 = float3(a0.xy, h.x);
        float3 p1 = float3(a0.zw, h.y);
        float3 p2 = float3(a1.xy, h.z);
        float3 p3 = float3(a1.zw, h.w);

        float4 norm = taylorInvSqrt(float4(dot(p0, p0), dot(p1, p1), dot(p2, p2), dot(p3, p3)));
        p0 *= norm.x;
        p1 *= norm.y;
        p2 *= norm.z;
        p3 *= norm.w;

        float4 m = max(0.6 - float4(dot(x0, x0), dot(x1, x1), dot(x2, x2), dot(x3, x3)), 0.0);
        m = m * m;
        return 42.0 * dot(m * m, float4(dot(p0, x0), dot(p1, x1), dot(p2, x2), dot(p3, x3)));
    }

    float topoField(float2 p, float t) {
        float2 orbitA = float2(sin(t * 0.11), cos(t * 0.09)) * 0.22;
        float2 orbitB = float2(cos(t * 0.07 + 1.4), sin(t * 0.13 + 0.6)) * 0.16;
        float2 orbitC = float2(sin(t * 0.05 + 2.2), cos(t * 0.08 + 0.9)) * 0.12;

        float2 sheared = float2(p.x + p.y * 0.40, p.y - p.x * 0.30);
        float2 stretched = float2((sheared + orbitA).x * 1.70, (sheared + orbitA).y * 0.50);
        float z = t * 0.045;

        float n0 = simplex3(float3(stretched * 0.22 + orbitC, z + 0.6));
        float2 slowWarp = float2(n0 * 0.75, -n0 * 0.55);

        float n1 = simplex3(float3(
            stretched * float2(1.15, 0.85) + slowWarp + orbitB,
            z * 0.55 + 1.3
        ));
        float2 skewed = float2(
            stretched.x * 0.45 + stretched.y * 0.95,
            stretched.y * 0.55 - stretched.x * 0.70
        );
        float n2 = simplex3(float3(
            skewed - slowWarp * 1.25 - orbitB * 1.1 + float2(5.2, -1.7),
            z * 0.4 + 2.8
        ));

        return (n1 * 1.25 + n2 * 1.05 + n0 * 0.28 + n1 * n2 * 0.12) * 1.48;
    }

    half4 main(float2 fragCoord) {
        float zoom = max(scale, 0.001);
        float2 p = ((fragCoord / density) + seed) / (200.0 * zoom) * waveScale;
        float t = iTime * speed;

        float pixelInP = waveScale / (200.0 * max(density, 0.001) * zoom);
        float blurR = pixelInP * 6.0;

        float f0 = topoField(p, t);
        float fX = topoField(p + float2(blurR, 0.0), t);
        float fY = topoField(p + float2(0.0, blurR), t);
        float field = (f0 + f0 + fX + fY) * 0.25;

        float lines = fract(field);
        float dist = min(lines, 1.0 - lines);
        float grad = max(length(float2(fX - f0, fY - f0)) / blurR * pixelInP, 1.0e-5);
        float distPx = dist / grad;
        float spacingPx = 1.0 / grad;

        float halfPx = min(max(lineWeight / grad, 2.5), spacingPx * 0.24);
        float softPx = min(max(halfPx * 1.1, 2.0), spacingPx * 0.28);
        float topo = clamp(
            (1.0 - smoothstep(halfPx - softPx * 0.5, halfPx + softPx * 0.5, distPx))
                * smoothstep(4.5, 11.0, spacingPx),
            0.0,
            1.0
        );

        return mix(bgColor, lineColor, half(topo));
    }
""".trimIndent()
