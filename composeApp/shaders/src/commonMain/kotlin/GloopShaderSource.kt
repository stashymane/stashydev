internal val GloopShaderSource = """
    uniform float iTime;
    uniform float2 iResolution;
    uniform float density;
    uniform float2 seed;
    uniform float speed;
    uniform float waveScale;
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

    // 3D simplex — advancing Z morphs the field in place (no XY drift).
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

        float n_ = 0.142857142857;
        float3 ns = n_ * D.wyz - D.xzx;

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
        // Z advances with time — primary in-place morph (kept slow).
        float z = t * 0.12;
        // Small XY orbits add secondary variation without net drift.
        float2 orbitA = float2(sin(t * 0.18), cos(t * 0.15)) * 0.12;
        float2 orbitB = float2(cos(t * 0.13), sin(t * 0.14)) * 0.09;
        float2 orbitC = float2(sin(t * 0.11 + 1.7), cos(t * 0.12 + 0.9)) * 0.07;

        float warp0 = simplex3(float3((p + orbitA) * 0.85, z));
        float2 warped = p + float2(warp0 * 0.7, warp0 * 0.45);

        // Two mid-frequency layers — denser isolines than the sparse setup.
        float n1 = simplex3(float3((warped + orbitB) * 1.0, z * 1.07));
        float n2 = simplex3(float3(
            (warped + orbitC) * 0.55 + float2(5.2, 1.3),
            z * 0.83 + 2.1
        ));

        // Moderate amplitude — enough bands without extreme packing.
        return n1 * 1.25 + n2 * 0.85;
    }

    half4 main(float2 fragCoord) {
        float2 worldPos = (fragCoord / density) + seed;
        float2 p = worldPos / 200.0 * waveScale;
        float t = iTime * speed;

        float pixelInP = waveScale / (200.0 * max(density, 0.001));
        // Low-pass the field over a few pixels so isoline corners round off.
        float blurR = pixelInP * 6.0;
        float f0 = topoField(p, t);
        float fX = topoField(p + float2(blurR, 0.0), t);
        float fXn = topoField(p - float2(blurR, 0.0), t);
        float fY = topoField(p + float2(0.0, blurR), t);
        float fYn = topoField(p - float2(0.0, blurR), t);
        float field = (f0 + fX + fXn + fY + fYn) * 0.2;

        // Distance to nearest contour in field space (0 at the streak center).
        float lines = fract(field);
        float dist = min(lines, 1.0 - lines);

        // |∇field| per pixel from the same taps used for softening.
        float grad = max(
            length(float2(fX - fXn, fY - fYn)) / (2.0 * blurR) * pixelInP,
            1.0e-5
        );
        float distPx = dist / grad;

        // Spacing between neighboring isolines in pixels (~1 / |∇field|).
        float spacingPx = 1.0 / grad;
        // Keep a clear gap: stroke+softness stays short of the midpoint (0.5 * spacing).
        float halfPx = min(max(lineWeight / grad, 3.0), spacingPx * 0.18);
        float softPx = min(max(halfPx * 1.15, 2.5), spacingPx * 0.22);
        float topo = 1.0 - smoothstep(halfPx - softPx * 0.5, halfPx + softPx * 0.5, distPx);
        topo = clamp(topo, 0.0, 1.0);
        // Fade out when bands pack too tightly so they don't merge into solid fills.
        float separation = smoothstep(8.0, 18.0, spacingPx);
        topo *= separation;

        return mix(bgColor, lineColor, half(topo));
    }
""".trimIndent()
