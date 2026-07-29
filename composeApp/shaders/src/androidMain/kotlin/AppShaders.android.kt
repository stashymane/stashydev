import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.toArgb

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ShaderHandle private constructor(
    private val shader: android.graphics.Shader
) {
    actual companion object {
        actual fun create(source: String): ShaderHandle =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ShaderHandle(RuntimeShader(source))
            } else {
                ShaderHandle(
                    android.graphics.LinearGradient(
                        0f, 0f, 1f, 1f,
                        android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT,
                        android.graphics.Shader.TileMode.CLAMP
                    )
                )
            }
    }

    private inline fun withRuntimeShader(block: RuntimeShader.() -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (shader as RuntimeShader).block()
        }
    }

    internal actual fun set(param: String, value: Int) = withRuntimeShader {
        setIntUniform(param, value)
    }

    internal actual fun set(param: String, value: Float) = withRuntimeShader {
        setFloatUniform(param, value)
    }

    internal actual fun set(param: String, value1: Float, value2: Float) = withRuntimeShader {
        setFloatUniform(param, value1, value2)
    }

    internal actual fun set(
        param: String,
        value1: Float,
        value2: Float,
        value3: Float,
        value4: Float
    ) = withRuntimeShader {
        setFloatUniform(param, value1, value2, value3, value4)
    }

    internal actual fun setColor(param: String, color: Color) = withRuntimeShader {
        setColorUniform(param, color.toArgb())
    }

    actual fun asComposeShader(): Shader = shader
}
