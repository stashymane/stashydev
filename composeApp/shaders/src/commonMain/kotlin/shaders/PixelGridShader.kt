package shaders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import colorUniform
import float2Uniform
import floatUniform
import source.ShaderSources

class PixelGridShader internal constructor(internal val handle: ShaderHandle) : ShaderConfig {
    var time by handle.floatUniform("iTime", 0f)
    var resolution by handle.float2Uniform("iResolution", 0f to 0f)
    var density by handle.floatUniform("density", 1f)
    var seed by handle.float2Uniform("seed", 0f to 0f)
    var speed by handle.floatUniform("speed", 1f)
    var pixelSize by handle.floatUniform("pixelSize", 8f)
    var gap by handle.floatUniform("gap", 2f)
    var scale by handle.floatUniform("scale", 1f)
    var bloomRadius by handle.floatUniform("bloomRadius", 40f)
    var bloomIntensity by handle.floatUniform("bloomIntensity", 0.3f)
    var bloomThreshold by handle.floatUniform("bloomThreshold", 0.45f)
    var randomAmount by handle.floatUniform("randomAmount", 0.2f)
    var bgColor by handle.colorUniform("bgColor", Color.Transparent)
    var color1 by handle.colorUniform("color1", Color.White)
    var color2 by handle.colorUniform("color2", Color.White)
    var color3 by handle.colorUniform("color3", Color.White)
    var color4 by handle.colorUniform("color4", Color.White)

    override fun asComposeShader(): Shader = handle.asComposeShader()

    companion object {
        fun create(): PixelGridShader =
            PixelGridShader(ShaderHandle.create(ShaderSources.PixelGrid))
    }
}

@Composable
fun rememberPixelGridShader(): PixelGridShader = remember { PixelGridShader.create() }
