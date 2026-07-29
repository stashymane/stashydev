import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.asComposeShader
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ShaderHandle(
    private val builder: RuntimeShaderBuilder
) {
    actual companion object {
        actual fun create(source: String): ShaderHandle =
            ShaderHandle(RuntimeShaderBuilder(RuntimeEffect.makeForShader(source)))
    }

    internal actual fun set(param: String, value: Int) = builder.uniform(param, value)
    internal actual fun set(param: String, value: Float) = builder.uniform(param, value)
    internal actual fun set(param: String, value1: Float, value2: Float) =
        builder.uniform(param, value1, value2)

    internal actual fun set(
        param: String,
        value1: Float,
        value2: Float,
        value3: Float,
        value4: Float
    ) = builder.uniform(param, value1, value2, value3, value4)

    internal actual fun setColor(param: String, color: Color) {
        builder.uniform(param, color.red, color.green, color.blue, color.alpha)
    }

    actual fun asComposeShader(): Shader = builder.makeShader().asComposeShader()
}
