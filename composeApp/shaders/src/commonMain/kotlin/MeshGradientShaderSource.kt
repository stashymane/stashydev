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

    // Soft value noise for organic warp.
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

    // Animated mesh control point anchored to a quadrant so neighbors stay apart.
    float2 meshPoint(float idx, float t) {
        float2 corner = float2(
            (idx < 1.5) ? 0.26 : 0.74,
            (idx == 0.0 || idx == 2.0) ? 0.26 : 0.74
        );
        // Mild per-seed jitter of the anchor — not enough to leave the quadrant.
        float2 jitter = (hash2(float2(idx, seed.x * 0.001 + 2.3)) - 0.5) * 0.06;
        float2 phase = hash2(float2(idx * 3.1, seed.y * 0.001 + 4.2));
        // Orbit radius stays well below half the quadrant gap (~0.48).
        float2 amp = 0.10 + 0.04 * hash2(float2(idx * 7.3, seed.x + seed.y));

        float2 offset = float2(
            sin(t * (0.35 + phase.x * 0.45) + phase.x * 6.2831853) * amp.x,
            cos(t * (0.28 + phase.y * 0.4) + phase.y * 6.2831853) * amp.y
        );

        return clamp(corner + jitter + offset, float2(0.08), float2(0.92));
    }

    // Soft pairwise push so any near-miss still keeps a minimum gap.
    float2 separate(float2 a, float2 b, float minDist) {
        float2 delta = a - b;
        float dist = length(delta);
        if (dist >= minDist || dist < 0.0001) {
            return a;
        }
        float2 dir = delta / dist;
        return a + dir * (minDist - dist) * 0.5;
    }

    half4 main(float2 fragCoord) {
        float2 viewSize = iResolution / max(density, 0.001);
        float aspect = viewSize.x / max(viewSize.y, 0.001);
        float2 uv = fragCoord / iResolution;
        float t = iTime * speed;

        // Mild domain warp so blends feel like a living mesh.
        float wAmt = clamp(warp, 0.0, 1.0);
        if (wAmt > 0.001) {
            float2 q = uv * (2.5 / max(scale, 0.1)) + seed * 0.0003;
            float2 warpOff = float2(
                valueNoise(q + float2(t * 0.08, 0.0)),
                valueNoise(q + float2(5.2, t * 0.07))
            );
            uv += (warpOff - 0.5) * wAmt * 0.18;
        }

        // Aspect-correct distance so blobs stay round on wide/tall cards.
        float2 uvA = float2(uv.x * aspect, uv.y);

        float2 p1 = meshPoint(0.0, t);
        float2 p2 = meshPoint(1.0, t);
        float2 p3 = meshPoint(2.0, t);
        float2 p4 = meshPoint(3.0, t);

        // Enforce a floor distance in UV space before aspect correction.
        float minDist = 0.28;
        p1 = separate(p1, p2, minDist);
        p1 = separate(p1, p3, minDist);
        p1 = separate(p1, p4, minDist);
        p2 = separate(p2, p1, minDist);
        p2 = separate(p2, p3, minDist);
        p2 = separate(p2, p4, minDist);
        p3 = separate(p3, p1, minDist);
        p3 = separate(p3, p2, minDist);
        p3 = separate(p3, p4, minDist);
        p4 = separate(p4, p1, minDist);
        p4 = separate(p4, p2, minDist);
        p4 = separate(p4, p3, minDist);

        p1 = float2(p1.x * aspect, p1.y);
        p2 = float2(p2.x * aspect, p2.y);
        p3 = float2(p3.x * aspect, p3.y);
        p4 = float2(p4.x * aspect, p4.y);

        // Softness maps to falloff power: higher = tighter lobes, still soft-cored.
        float soft = clamp(softness, 0.05, 1.0);
        float power = mix(1.1, 2.8, soft) / max(scale, 0.05);
        // Soft core prevents infinite peaks / hard ridges if points ever approach.
        float core = 0.12 * max(scale, 0.1);

        float d1 = length(uvA - p1);
        float d2 = length(uvA - p2);
        float d3 = length(uvA - p3);
        float d4 = length(uvA - p4);

        float w1 = 1.0 / pow(d1 * d1 + core * core, power * 0.5);
        float w2 = 1.0 / pow(d2 * d2 + core * core, power * 0.5);
        float w3 = 1.0 / pow(d3 * d3 + core * core, power * 0.5);
        float w4 = 1.0 / pow(d4 * d4 + core * core, power * 0.5);
        float wSum = w1 + w2 + w3 + w4;

        half4 col = (color1 * half(w1) + color2 * half(w2)
                   + color3 * half(w3) + color4 * half(w4)) / half(wSum);

        return col;
    }
""".trimIndent()
