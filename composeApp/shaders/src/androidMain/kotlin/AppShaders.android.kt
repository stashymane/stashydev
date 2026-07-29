import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.ui.graphics.Shader

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AppShader private constructor(
    private val shader: android.graphics.Shader
) : ShaderParams {
    actual companion object {
        actual fun createGloop(): AppShader = from(ShaderSources.Gloop)
        actual fun createTest(): AppShader = from(ShaderSources.Test)

        fun from(src: String): AppShader = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppShader(RuntimeShader(src))
        } else {
            AppShader(
                android.graphics.LinearGradient(
                    0f, 0f, 1f, 1f,
                    android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT,
                    android.graphics.Shader.TileMode.CLAMP
                )
            )
        }
    }

    actual override fun set(param: String, value: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (shader as RuntimeShader).setIntUniform(param, value)
        }
    }

    actual override fun set(param: String, value: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (shader as RuntimeShader).setFloatUniform(param, value)
        }
    }

    actual override fun set(param: String, value1: Float, value2: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (shader as RuntimeShader).setFloatUniform(param, value1, value2)
        }
    }

    actual fun asComposeShader(): Shader = shader
}
