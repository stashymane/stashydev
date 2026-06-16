import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shader

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class AppShader : ShaderParams {
    companion object {
        fun createGloop(): AppShader
        fun createTest(): AppShader
    }

    override fun set(param: String, value: Int)
    override fun set(param: String, value: Float)
    override fun set(param: String, value1: Float, value2: Float)

    fun asComposeShader(): Shader
}

interface ShaderParams {
    fun set(param: String, value: Int)
    fun set(param: String, value: Float)
    fun set(param: String, value1: Float, value2: Float)
}

@Composable
fun AppShader.Companion.gloop(): AppShader = remember { createGloop() }

@Composable
fun AppShader.Companion.test(): AppShader = remember { createTest() }

