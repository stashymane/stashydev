package shaders

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ShaderHandle {
    companion object {
        fun create(source: String): ShaderHandle
    }

    internal fun set(param: String, value: Int)
    internal fun set(param: String, value: Float)
    internal fun set(param: String, value1: Float, value2: Float)
    internal fun set(param: String, value1: Float, value2: Float, value3: Float, value4: Float)

    /** Color uniforms declared with `layout(color)` — Android requires setColorUniform. */
    internal fun setColor(param: String, color: Color)
    fun asComposeShader(): Shader
}
