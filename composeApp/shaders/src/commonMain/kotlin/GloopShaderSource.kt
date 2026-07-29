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

    const float2 noiseK = float2(0.3183099, 0.3678794);
    const float INV_289 = 1.0 / 289.0;

    float2 gradientNoiseDir(float2 x) {
        x = x * noiseK + noiseK.yx;
        return -1.0 + 2.0 * fract(16.0 * noiseK * fract(x.x * x.y * (x.x + x.y)));
    }

    float gradientNoise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float2 u = f * f * (3.0 - 2.0 * f);
        return mix(
            mix(dot(gradientNoiseDir(i), f),
                dot(gradientNoiseDir(i + float2(1.0, 0.0)), f - float2(1.0, 0.0)), u.x),
            mix(dot(gradientNoiseDir(i + float2(0.0, 1.0)), f - float2(0.0, 1.0)),
                dot(gradientNoiseDir(i + float2(1.0, 1.0)), f - float2(1.0, 1.0)), u.x), u.y
        );
    }

    float3 mod289v3(float3 x) { return x - floor(x * INV_289) * 289.0; }
    float2 mod289v2(float2 x) { return x - floor(x * INV_289) * 289.0; }
    float3 permute(float3 x) { return mod289v3(((x * 34.0) + 10.0) * x); }

    float simplex(float2 v) {
        const float C0 = 0.211324865405187;
        const float C1 = 0.366025403784439;
        const float C2 = -0.577350269189626;
        const float C3 = 0.024390243902439;
        float2 i = floor(v + dot(v, float2(C1)));
        float2 x0 = v - i + dot(i, float2(C0));
        float2 i1 = (x0.x > x0.y) ? float2(1.0, 0.0) : float2(0.0, 1.0);
        float4 x12 = float4(x0.x + C0 - i1.x, x0.y + C0 - i1.y, x0.x + C2, x0.y + C2);
        i = mod289v2(i);
        float3 p = permute(permute(i.y + float3(0.0, i1.y, 1.0)) + i.x + float3(0.0, i1.x, 1.0));
        float3 m = max(0.5 - float3(dot(x0, x0), dot(x12.xy, x12.xy), dot(x12.zw, x12.zw)), 0.0);
        m = m * m * m * m;
        float3 x1 = 2.0 * fract(p * C3) - 1.0;
        float3 h = abs(x1) - 0.5;
        float3 ox = floor(x1 + 0.5);
        float3 a0 = x1 - ox;
        m *= 1.79284291400159 - 0.85373472095314 * (a0 * a0 + h * h);
        float3 g;
        g.x = a0.x * x0.x + h.x * x0.y;
        g.y = a0.y * x12.x + h.y * x12.y;
        g.z = a0.z * x12.z + h.z * x12.w;
        return 130.0 * dot(m, g);
    }

    half4 main(float2 fragCoord) {
        float2 worldPos = (fragCoord / density) + seed;
        float2 p = worldPos / 200.0 * waveScale;
        float t = iTime * speed;

        float warp0 = simplex((p + float2(t * 0.04, t * -0.03)) * 1.2);
        float2 warped = p + float2(warp0 * 0.6, warp0 * 0.4);

        float n1 = gradientNoise((warped + float2(t * 0.06, t * -0.05)) * 2.5);
        float n2 = simplex((warped + float2(t * -0.04, t * 0.07)) * 1.8);
        float n3 = gradientNoise((warped + float2(t * 0.03, t * 0.02)) * 0.7);

        float field = n1 * 3.5 + n2 * 1.5 + n3 * 2.0;

        float lines = fract(field);
        float aa = waveScale * 3.0 / (iResolution.x / density);
        float topo = smoothstep(lineWeight + aa, lineWeight - aa, lines)
                   + smoothstep(1.0 - lineWeight + aa, 1.0 - lineWeight - aa, lines);
        topo = clamp(topo, 0.0, 1.0);

        return mix(bgColor, lineColor, half(topo));
    }
""".trimIndent()
