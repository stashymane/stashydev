internal val PixelGridShaderSource = """
    uniform float iTime;
    uniform float2 iResolution;
    uniform float density;
    uniform float2 seed;
    uniform float speed;
    uniform float pixelSize;
    uniform float gap;
    uniform float scale;
    uniform float bloomRadius;
    uniform float bloomIntensity;
    uniform float bloomThreshold;
    uniform float randomAmount;
    layout(color) uniform half4 bgColor;
    layout(color) uniform half4 color1;
    layout(color) uniform half4 color2;
    layout(color) uniform half4 color3;
    layout(color) uniform half4 color4;

    float hash(float2 p) {
        float3 p3 = fract(float3(p.x, p.y, p.x) * 0.1031);
        p3 += dot(p3, float3(p3.y + 33.33, p3.z + 33.33, p3.x + 33.33));
        return fract((p3.x + p3.y) * p3.z);
    }

    float hash3(float3 p) {
        p = fract(p * 0.1031);
        p += dot(p, float3(p.y + 33.33, p.z + 33.33, p.x + 33.33));
        return fract((p.x + p.y) * p.z);
    }

    float valueNoise3(float3 p) {
        float3 i = floor(p);
        float3 f = fract(p);
        float3 u = f * f * (3.0 - 2.0 * f);

        float n000 = hash3(i);
        float n100 = hash3(i + float3(1.0, 0.0, 0.0));
        float n010 = hash3(i + float3(0.0, 1.0, 0.0));
        float n110 = hash3(i + float3(1.0, 1.0, 0.0));
        float n001 = hash3(i + float3(0.0, 0.0, 1.0));
        float n101 = hash3(i + float3(1.0, 0.0, 1.0));
        float n011 = hash3(i + float3(0.0, 1.0, 1.0));
        float n111 = hash3(i + float3(1.0, 1.0, 1.0));

        float nx00 = mix(n000, n100, u.x);
        float nx10 = mix(n010, n110, u.x);
        float nx01 = mix(n001, n101, u.x);
        float nx11 = mix(n011, n111, u.x);
        return mix(mix(nx00, nx10, u.y), mix(nx01, nx11, u.y), u.z);
    }

    float toneShape(float a) {
        a = clamp(a, 0.0, 1.0);
        a = clamp(0.5 + (a - 0.5) * 1.20, 0.0, 1.0);
        if (a < 0.5) {
            float t = a * 2.0;
            t = t * t * (3.0 - 2.0 * t);
            return mix(0.10, 0.50, t);
        }
        float t = (a - 0.5) * 2.0;
        float body = t * (1.0 - 0.32 * t);
        float midHi = mix(0.50, 0.72, clamp(body, 0.0, 1.0));
        return mix(midHi, 1.0, smoothstep(0.88, 1.0, t));
    }

    half4 paletteColor(float amp) {
        float a = clamp(amp, 0.0, 1.0) * 3.0;
        float idx = floor(a);
        float blend = fract(a);
        half4 c0;
        half4 c1;
        if (idx < 1.0) { c0 = color1; c1 = color2; }
        else if (idx < 2.0) { c0 = color2; c1 = color3; }
        else { c0 = color3; c1 = color4; }
        return mix(c0, c1, half(blend));
    }

    float2 viewUV(float2 worldCenter, float2 viewSize) {
        return worldCenter / max(viewSize, float2(0.001));
    }

    // Zooms the color field only — pixelSize/gap are unchanged.
    float2 fieldCoord(float2 uv01) {
        return (uv01 - 0.5) * (2.6 / max(scale, 0.001)) + seed * 0.001;
    }

    float morphField(float2 uv01, float z, float t) {
        float2 p = fieldCoord(uv01);
        float2 drift = float2(
            sin(t * 0.21 + seed.x * 0.001),
            cos(t * 0.17 + seed.y * 0.001)
        ) * 0.45;

        float low = valueNoise3(float3(p * 0.40 + drift * 0.5, z * 0.50));
        float mid = valueNoise3(float3(p * 0.75 + drift, z));
        float breath = 0.5 + 0.14 * sin(t * 0.32 + low * 1.4);

        float field = mix(mix(low, mid, 0.20), breath, 0.06);
        return toneShape(field);
    }

    float pixelAmplitude(float2 cell, float2 viewSize, float z, float t) {
        float h = hash(cell + seed);
        float rnd = 0.5 + 0.5 * sin(h * 6.2831853 + t * 1.1);
        float2 uv01 = viewUV((cell + 0.5) * float2(pixelSize + gap), viewSize);
        float wave = morphField(uv01, z + h * 0.04, t);
        float r = clamp(randomAmount, 0.0, 1.0) * 0.65;
        r *= 1.0 - smoothstep(0.55, 0.82, wave);
        return clamp(rnd * r + wave * (1.0 - r), 0.0, 1.0);
    }

    float bloomAmplitude(float2 cell, float2 viewSize, float z, float t) {
        float h = hash(cell + seed);
        float rnd = 0.5 + 0.5 * sin(h * 6.2831853 + t * 1.1);
        float2 uv01 = viewUV((cell + 0.5) * float2(pixelSize + gap), viewSize);
        float2 p = fieldCoord(uv01);
        float2 drift = float2(sin(t * 0.21), cos(t * 0.17)) * 0.45;
        float wave = valueNoise3(float3(p * 0.40 + drift * 0.5, z * 0.50 + h * 0.04));
        float r = clamp(randomAmount, 0.0, 1.0) * 0.65;
        return clamp(rnd * r + wave * (1.0 - r), 0.0, 1.0);
    }

    half4 main(float2 fragCoord) {
        float2 worldPos = fragCoord / density;
        float cellSize = pixelSize + gap;
        float2 cellCoord = floor(worldPos / cellSize);
        float2 cellLocal = mod(worldPos, cellSize);
        float t = iTime * speed;
        float z = t * 0.42;

        float halfGap = gap * 0.5;
        float inPixel = step(halfGap, cellLocal.x)
                      * step(cellLocal.x, cellSize - halfGap)
                      * step(halfGap, cellLocal.y)
                      * step(cellLocal.y, cellSize - halfGap);

        float2 viewSize = iResolution / max(density, 0.001);
        half4 thisColor = bgColor;
        if (inPixel > 0.5) {
            thisColor = paletteColor(pixelAmplitude(cellCoord, viewSize, z, t));
        }

        half4 bloom = half4(0.0);
        if (bloomIntensity > 0.0001 && bloomRadius > 0.5) {
            float thresh = clamp(bloomThreshold, 0.0, 0.999);
            float span = max(1.0 - thresh, 1.0e-4);
            float radiusSq = bloomRadius * bloomRadius;
            float rangeF = min(floor(bloomRadius / max(cellSize, 0.001)) + 1.0, 5.0);

            for (int iy = -5; iy <= 5; iy += 2) {
                if (abs(float(iy)) > rangeF) { continue; }
                for (int ix = -5; ix <= 5; ix += 2) {
                    if (abs(float(ix)) > rangeF) { continue; }

                    float2 neighbor = cellCoord + float2(float(ix), float(iy));
                    float2 delta = worldPos - (neighbor + 0.5) * cellSize;
                    float distSq = dot(delta, delta);
                    if (distSq >= radiusSq) { continue; }

                    float amp = bloomAmplitude(neighbor, viewSize, z, t);
                    float tAmp = clamp((amp - thresh) / span, 0.0, 1.0);
                    float curve = tAmp * tAmp * (3.0 - 2.0 * tAmp);
                    curve *= curve;
                    if (curve <= 0.0) { continue; }

                    float w = 1.0 - distSq / radiusSq;
                    w = w * w * curve * 2.0;
                    w *= 1.0 - 0.55 * smoothstep(0.65, 1.0, amp);

                    float colorAmp = mix(min(amp, 0.66), amp, smoothstep(0.92, 1.0, amp));
                    bloom += (paletteColor(colorAmp) - bgColor) * half(w);
                }
            }
        }

        return thisColor + bloom * half(bloomIntensity);
    }
""".trimIndent()
