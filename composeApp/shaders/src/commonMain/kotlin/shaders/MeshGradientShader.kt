package shaders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import colorUniform
import float2Uniform
import floatUniform
import source.ShaderSources

class MeshGradientShader internal constructor(internal val handle: ShaderHandle) : ShaderConfig {
    var time by handle.floatUniform("iTime", 0f)
    var resolution by handle.float2Uniform("iResolution", 0f to 0f)
    var density by handle.floatUniform("density", 1f)
    var seed by handle.float2Uniform("seed", 0f to 0f)
    var speed by handle.floatUniform("speed", 0.4f)
    var scale by handle.floatUniform("scale", 1f)
    var softness by handle.floatUniform("softness", 0.55f)
    var warp by handle.floatUniform("warp", 0.35f)
    var color1 by handle.colorUniform("color1", Color.White)
    var color2 by handle.colorUniform("color2", Color.White)
    var color3 by handle.colorUniform("color3", Color.White)
    var color4 by handle.colorUniform("color4", Color.White)

    override fun asComposeShader(): Shader = handle.asComposeShader()

    companion object {
        fun create(): MeshGradientShader =
            MeshGradientShader(ShaderHandle.create(ShaderSources.MeshGradient))
    }
}

@Composable
fun rememberMeshGradientShader(): MeshGradientShader =
    remember { MeshGradientShader.create() }
