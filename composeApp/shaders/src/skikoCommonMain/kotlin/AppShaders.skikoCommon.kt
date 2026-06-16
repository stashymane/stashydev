import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.asComposeShader
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AppShader(
    val builder: RuntimeShaderBuilder
) : ShaderParams {
    actual companion object {
        actual fun createGloop(): AppShader = from(ShaderSources.Gloop)
        actual fun createTest(): AppShader = from(ShaderSources.Test)

        fun from(src: String) = AppShader(RuntimeShaderBuilder(RuntimeEffect.makeForShader(src)))
    }

    actual override fun set(param: String, value: Int) = builder.uniform(param, value)
    actual override fun set(param: String, value: Float) = builder.uniform(param, value)
    actual override fun set(param: String, value1: Float, value2: Float) = builder.uniform(param, value1, value2)

    actual fun asComposeShader(): Shader = builder.makeShader().asComposeShader()
}
