package theme

import org.jetbrains.skia.RuntimeEffect

object Shaders {
    val Test = RuntimeEffect.makeForShader(
        """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord/resolution.xy;

        float mixValue = distance(uv, vec2(0, 1));
        return mix(color, color2, mixValue);
    }
""".trimIndent()
    )

    val Pattern = RuntimeEffect.makeForShader(
        """
        uniform float iTime;
        uniform float Warp2X;
        uniform float Warp2Y;
        uniform float WarpSpeedX;
        uniform float WarpSpeedY;
        uniform float RingStrength;
        uniform float2 iResolution;

        // Pre-computed constants
        const float2 noiseK = float2(0.3183099, 0.3678794);
        const float4 simplexC = float4(0.211324865405187, 0.366025403784439, -0.577350269189626, 0.024390243902439);
        const float INV_289 = 1.0 / 289.0;

        float2 gradientNoiseDir(float2 x) {
            x = x * noiseK + noiseK.yx;
            return -1.0 + 2.0 * fract(16.0 * noiseK * fract(x.x * x.y * (x.x + x.y)));
        }

        float gradientNoise(float2 v, float scale) {
            float2 p = v * scale;
            float2 i = floor(p);
            float2 f = fract(p);
            float2 u = f * f * (3.0 - 2.0 * f);
            return mix(
                mix(dot(gradientNoiseDir(i), f),
                    dot(gradientNoiseDir(i + float2(1.0, 0.0)), f - float2(1.0, 0.0)), u.x),
                mix(dot(gradientNoiseDir(i + float2(0.0, 1.0)), f - float2(0.0, 1.0)),
                    dot(gradientNoiseDir(i + float2(1.0, 1.0)), f - float2(1.0, 1.0)), u.x), u.y
            ) * 0.5 + 0.5;
        }

        float3 mod289(float3 x) {
            return x - floor(x * INV_289) * 289.0;
        }

        float2 mod289_vec2(float2 x) {
            return x - floor(x * INV_289) * 289.0;
        }

        float3 permute(float3 x) {
            return mod289(((x * 34.0) + 1.0) * x);
        }

        float simplexNoise(float2 v, float scale) {
            v *= scale;
            float2 i = floor(v + dot(v, simplexC.yy));
            float2 x0 = v - i + dot(i, simplexC.xx);
            float2 i1 = (x0.x > x0.y) ? float2(1.0, 0.0) : float2(0.0, 1.0);
            float4 x12 = x0.xyxy + simplexC.xxzz;
            x12.xy -= i1;
            i = mod289_vec2(i);
            float3 p = permute(permute(i.y + float3(0.0, i1.y, 1.0)) + i.x + float3(0.0, i1.x, 1.0));
            float3 m = max(0.5 - float3(dot(x0, x0), dot(x12.xy, x12.xy), dot(x12.zw, x12.zw)), 0.0);
            m = m * m * m * m;
            float3 x = 2.0 * fract(p * simplexC.www) - 1.0;
            float3 h = abs(x) - 0.5;
            float3 ox = floor(x + 0.5);
            float3 a0 = x - ox;
            m *= 1.79284291400159 - 0.85373472095314 * (a0 * a0 + h * h);
            float3 g;
            g.x = a0.x * x0.x + h.x * x0.y;
            g.yz = a0.yz * x12.xz + h.yz * x12.yw;
            return (130.0 * dot(m, g)) * 0.5 + 0.5;
        }

        float antiAliasedBox(float2 uv, float width, float height) {
            float2 pixelSize = 1.0 / iResolution.xy;
            float2 d = abs(uv * 2.0 - 1.0) - float2(width, height);
            d = 1.0 - d / pixelSize;
            return clamp(min(d.x, d.y), 0.0, 1.0);
        }

        half4 main(float2 fragCoord) {
            float2 uv = fragCoord / iResolution.xy;
            
            // Center coordinates and apply aspect correction
            uv = (uv - 0.5) * 2.0;  // Convert to -1 to 1 range
            float aspectRatio = iResolution.x / iResolution.y;
            uv.x *= aspectRatio;    // Correct aspect ratio
            
            // Rest of the shader with centered coordinates
            float2 warp1 = uv + (iTime * float2(Warp2X, Warp2Y));
            float2 warp2 = uv + (iTime * float2(WarpSpeedX, WarpSpeedY));
            
            float noise1 = gradientNoise(warp1, 0.75) * 10.0;
            float noise2 = simplexNoise(warp2, 0.5) * 2.0;
            
            float ringDistance = length(uv) * RingStrength;
            
            float pattern = fract(noise1 + (noise2 * ringDistance));
            float intensity = antiAliasedBox(float2(pattern), 0.2, 1.0) * 0.015;
            
            return half4(half3(intensity), intensity);
        }


    """.trimIndent()
    )

}
