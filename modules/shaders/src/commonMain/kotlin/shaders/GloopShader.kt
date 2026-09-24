package shaders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import colorUniform
import float2Uniform
import floatUniform
import source.ShaderSources

class GloopShader internal constructor(internal val handle: ShaderHandle) : ShaderConfig {
    var time by handle.floatUniform("iTime", 0f)
    var density by handle.floatUniform("density", 1f)
    var seed by handle.float2Uniform("seed", 0f to 0f)
    var speed by handle.floatUniform("speed", 1f)
    var waveScale by handle.floatUniform("waveScale", 1f)
    var scale by handle.floatUniform("scale", 1f)
    var lineWeight by handle.floatUniform("lineWeight", 1f)
    var bgColor by handle.colorUniform("bgColor", Color.Transparent)
    var lineColor by handle.colorUniform("lineColor", Color.White)

    override fun asComposeShader(): Shader = handle.asComposeShader()

    companion object {
        fun create(): GloopShader = GloopShader(ShaderHandle.create(ShaderSources.Gloop))
    }
}

@Composable
fun rememberGloopShader(): GloopShader = remember { GloopShader.create() }
