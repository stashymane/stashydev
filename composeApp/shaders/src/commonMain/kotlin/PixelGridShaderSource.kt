internal val PixelGridShaderSource = """
    uniform float iTime;
    uniform float2 iResolution;
    uniform float density;
    uniform float2 seed;
    uniform float speed;
    uniform float pixelSize;
    uniform float gap;
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

    // 3D value noise — advancing Z morphs the field in place (no XY drift).
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
        float nxy0 = mix(nx00, nx10, u.y);
        float nxy1 = mix(nx01, nx11, u.y);
        return mix(nxy0, nxy1, u.z);
    }

    float fbm3(float2 p, float z) {
        float v = 0.0;
        float amp = 0.5;
        float freq = 1.0;
        float3 q = float3(p, z);
        for (int i = 0; i < 2; i++) {
            v += amp * valueNoise3(q * freq);
            freq *= 2.0;
            amp *= 0.5;
            q += float3(3.17, 1.31, 2.47);
        }
        return v / 0.75;
    }

    // Soft mesh-gradient field — lobes morph in place (appear/disappear).
    float morphField(float2 p) {
        float2 viewSize = iResolution / max(density, 0.001);
        float scale = min(viewSize.x, viewSize.y);

        float2 uv = (p - viewSize * 0.5) / scale + seed * 0.0007;
        float z = iTime * speed * 0.35;

        // Mild in-place warp (also evolved in Z, not translated in XY).
        float2 warp = float2(
            fbm3(uv * 1.35 + float2(0.0, 0.0), z),
            fbm3(uv * 1.35 + float2(5.2, 1.3), z * 1.07)
        );

        float n = fbm3(uv * 1.35 + (warp - 0.5) * 0.25, z);

        return clamp(n, 0.0, 1.0);
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
        return mix(c0, c1, half(smoothstep(0.0, 1.0, blend)));
    }

    float pixelAmplitude(float2 cell, float t) {
        float2 cellSize = float2(pixelSize + gap, pixelSize + gap);
        float2 worldCenter = (cell + 0.5) * cellSize;

        // Subtle per-pixel random motion (small contribution).
        float h = hash(cell + seed);
        float rnd = 0.5 + 0.5 * sin(h * 6.2831853 + t * 1.4);

        // Morphing noise field (main contribution).
        float wave = morphField(worldCenter);

        float r = clamp(randomAmount, 0.0, 1.0);
        return clamp(rnd * r + wave * (1.0 - r), 0.0, 1.0);
    }

    half4 getPixelColor(float2 cell, float t) {
        return paletteColor(pixelAmplitude(cell, t));
    }

    half4 main(float2 fragCoord) {
        float2 worldPos = fragCoord / density;
        float cellSize = pixelSize + gap;
        float2 cellCoord = floor(worldPos / cellSize);
        float2 cellLocal = mod(worldPos, cellSize);

        float t = iTime * speed;

        float halfGap = gap * 0.5;
        float inPixel = step(halfGap, cellLocal.x)
                      * step(cellLocal.x, cellSize - halfGap)
                      * step(halfGap, cellLocal.y)
                      * step(cellLocal.y, cellSize - halfGap);

        half4 thisColor = getPixelColor(cellCoord, t);

        half4 bloom = half4(0.0);
        float thresh = clamp(bloomThreshold, 0.0, 0.999);
        float span = 1.0 - thresh;
        const int MAX_RANGE = 8;
        for (int iy = -MAX_RANGE; iy <= MAX_RANGE; iy++) {
            for (int ix = -MAX_RANGE; ix <= MAX_RANGE; ix++) {
                float2 neighbor = cellCoord + float2(float(ix), float(iy));
                float2 neighborCenter = (neighbor + 0.5) * cellSize;
                float dist = length(worldPos - neighborCenter);
                if (dist < bloomRadius) {
                    float amp = pixelAmplitude(neighbor, t);
                    // Soft ease-in above the threshold (not a hard gate).
                    float tAmp = clamp((amp - thresh) / span, 0.0, 1.0);
                    float curve = tAmp * tAmp * (3.0 - 2.0 * tAmp); // smoothstep
                    curve = curve * curve; // extra ease-in so it rises gradually
                    if (curve <= 0.0) { continue; }

                    float norm = dist / bloomRadius;
                    float w = (1.0 - norm * norm);
                    w = w * w * curve;
                    half4 nc = paletteColor(amp);
                    bloom += (nc - bgColor) * half(w);
                }
            }
        }

        half4 result = bgColor;
        if (inPixel > 0.5) {
            result = thisColor;
        }
        result = result + bloom * half(bloomIntensity);

        return result;
    }
""".trimIndent()
