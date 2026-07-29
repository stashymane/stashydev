package source

internal val MeshGradientShaderSource = """
    uniform float iTime;
    uniform float2 iResolution;
    uniform float density;
    uniform float2 seed;
    uniform float speed;
    uniform float scale;
    uniform float softness;
    uniform float warp;
    layout(color) uniform half4 color1;
    layout(color) uniform half4 color2;
    layout(color) uniform half4 color3;
    layout(color) uniform half4 color4;

    float hash(float2 p) {
        float3 p3 = fract(float3(p.x, p.y, p.x) * 0.1031);
        p3 += dot(p3, float3(p3.y + 33.33, p3.z + 33.33, p3.x + 33.33));
        return fract((p3.x + p3.y) * p3.z);
    }

    float2 hash2(float2 p) {
        return float2(hash(p), hash(p + float2(19.19, 7.7)));
    }

    float valueNoise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float2 u = f * f * (3.0 - 2.0 * f);
        float a = hash(i);
        float b = hash(i + float2(1.0, 0.0));
        float c = hash(i + float2(0.0, 1.0));
        float d = hash(i + float2(1.0, 1.0));
        return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    }

    float2 meshPoint(float idx, float t) {
        float2 corner = float2(
            (idx < 1.5) ? 0.26 : 0.74,
            (idx == 0.0 || idx == 2.0) ? 0.26 : 0.74
        );
        float2 jitter = (hash2(float2(idx, seed.x * 0.001 + 2.3)) - 0.5) * 0.04;
        float2 phase = hash2(float2(idx * 3.1, seed.y * 0.001 + 4.2));
        float2 amp = 0.18 + 0.08 * hash2(float2(idx * 7.3, seed.x + seed.y));
        float2 rate = float2(
            0.28 + phase.x * 0.50 + idx * 0.07,
            0.22 + phase.y * 0.45 + idx * 0.05
        );

        float2 offset = float2(
            sin(t * rate.x + phase.x * 6.2831853 + idx * 1.7) * amp.x
                + sin(t * rate.x * 1.73 + phase.y * 4.1) * amp.x * 0.40,
            cos(t * rate.y + phase.y * 6.2831853 + idx * 2.3) * amp.y
                + cos(t * rate.y * 2.11 + phase.x * 3.7) * amp.y * 0.40
        );

        return clamp(corner + jitter + offset, float2(0.08), float2(0.92));
    }

    float proximity(float2 a, float2 b) {
        return 1.0 - smoothstep(0.10, 0.36, length(a - b));
    }

    half4 main(float2 fragCoord) {
        float2 viewSize = iResolution / max(density, 0.001);
        float aspect = viewSize.x / max(viewSize.y, 0.001);
        float2 uv = fragCoord / iResolution;
        float t = iTime * speed;

        float wAmt = clamp(warp, 0.0, 1.0);
        if (wAmt > 0.001) {
            float2 q = uv * (2.4 / max(scale, 0.1)) + seed * 0.0003;
            float n = valueNoise(q + float2(t * 0.09, t * 0.07));
            float2 warpOff = float2(n, valueNoise(q + float2(5.2 + n, 3.1)));
            uv += (warpOff - 0.5) * wAmt * 0.16;
        }

        float2 uvA = float2(uv.x * aspect, uv.y);

        float2 p1 = meshPoint(0.0, t);
        float2 p2 = meshPoint(1.0, t);
        float2 p3 = meshPoint(2.0, t);
        float2 p4 = meshPoint(3.0, t);

        float prox = proximity(p1, p2);
        prox = max(prox, proximity(p1, p3));
        prox = max(prox, proximity(p1, p4));
        prox = max(prox, proximity(p2, p3));
        prox = max(prox, proximity(p2, p4));
        prox = max(prox, proximity(p3, p4));

        p1 = float2(p1.x * aspect, p1.y);
        p2 = float2(p2.x * aspect, p2.y);
        p3 = float2(p3.x * aspect, p3.y);
        p4 = float2(p4.x * aspect, p4.y);

        float soft = clamp(softness, 0.05, 1.0);
        float power = mix(0.70, 1.45, soft) / max(scale, 0.05);
        float s = max(scale, 0.1);
        float coreRad = mix(0.22, 0.40, prox) * s;
        float core2 = coreRad * coreRad;
        float halfPower = mix(power * 0.5, power * 0.30, prox);

        float b1 = 0.94 + 0.10 * sin(t * 0.41 + 0.2);
        float b2 = 0.94 + 0.10 * sin(t * 0.35 + 1.7);
        float b3 = 0.94 + 0.10 * sin(t * 0.47 + 3.1);
        float b4 = 0.94 + 0.10 * sin(t * 0.38 + 4.6);

        float3 lumW = float3(0.2126, 0.7152, 0.0722);
        float c1 = mix(1.0, 1.0 / sqrt(max(dot(float3(color1.r, color1.g, color1.b), lumW), 0.04)), 0.75);
        float c2 = mix(1.0, 1.0 / sqrt(max(dot(float3(color2.r, color2.g, color2.b), lumW), 0.04)), 0.75);
        float c3 = mix(1.0, 1.0 / sqrt(max(dot(float3(color3.r, color3.g, color3.b), lumW), 0.04)), 0.75);
        float c4 = mix(1.0, 1.0 / sqrt(max(dot(float3(color4.r, color4.g, color4.b), lumW), 0.04)), 0.75);

        float2 d1 = uvA - p1;
        float2 d2 = uvA - p2;
        float2 d3 = uvA - p3;
        float2 d4 = uvA - p4;

        float floorW = mix(0.18, 0.34, prox);
        float w1 = b1 * c1 * (1.0 / pow(dot(d1, d1) + core2, halfPower) + floorW);
        float w2 = b2 * c2 * (1.0 / pow(dot(d2, d2) + core2, halfPower) + floorW);
        float w3 = b3 * c3 * (1.0 / pow(dot(d3, d3) + core2, halfPower) + floorW);
        float w4 = b4 * c4 * (1.0 / pow(dot(d4, d4) + core2, halfPower) + floorW);
        float wSum = w1 + w2 + w3 + w4;

        return (color1 * half(w1) + color2 * half(w2)
              + color3 * half(w3) + color4 * half(w4)) / half(wSum);
    }
""".trimIndent()
