import androidx.compose.ui.graphics.Shader

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AppShader : ShaderParams {
    actual companion object {
        actual fun createGloop(): AppShader {
            TODO("Not yet implemented")
        }

        actual fun createTest(): AppShader {
            TODO("Not yet implemented")
        }

    }

    actual override fun set(param: String, value: Int) {
    }

    actual override fun set(param: String, value: Float) {
    }

    actual override fun set(param: String, value1: Float, value2: Float) {
    }

    actual fun asComposeShader(): Shader = TODO()
}
