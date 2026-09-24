package shaders

import androidx.compose.ui.graphics.Shader

internal interface ShaderConfig {
    fun asComposeShader(): Shader
}
