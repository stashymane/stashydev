import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

class GloopShader internal constructor(internal val handle: ShaderHandle) {
    var time: Float = 0f
    var density: Float = 1f
    var seed: Pair<Float, Float> = 0f to 0f
    var speed: Float = 1f
    var waveScale: Float = 1f
    var scale: Float = 1f
    var lineWeight: Float = 0.16f
    var bgColor: Color = Color.Transparent
    var lineColor: Color = Color.White

    fun apply() {
        handle.set("iTime", time)
        handle.set("density", density)
        handle.set("seed", seed.first, seed.second)
        handle.set("speed", speed)
        handle.set("waveScale", waveScale)
        handle.set("scale", scale)
        handle.set("lineWeight", lineWeight)
        handle.setColor("bgColor", bgColor)
        handle.setColor("lineColor", lineColor)
    }

    fun asComposeShader(): Shader = handle.asComposeShader()

    companion object {
        fun create(): GloopShader = GloopShader(ShaderHandle.create(ShaderSources.Gloop))
    }
}

class PixelGridShader internal constructor(internal val handle: ShaderHandle) {
    var time: Float = 0f
    var resolution: Pair<Float, Float> = 0f to 0f
    var density: Float = 1f
    var seed: Pair<Float, Float> = 0f to 0f
    var speed: Float = 1f
    var pixelSize: Float = 8f
    var gap: Float = 2f
    var scale: Float = 1f
    var bloomRadius: Float = 40f
    var bloomIntensity: Float = 0.3f
    var bloomThreshold: Float = 0.45f
    var randomAmount: Float = 0.2f
    var bgColor: Color = Color.Transparent
    var color1: Color = Color.White
    var color2: Color = Color.White
    var color3: Color = Color.White
    var color4: Color = Color.White

    fun apply() {
        handle.set("iTime", time)
        handle.set("iResolution", resolution.first, resolution.second)
        handle.set("density", density)
        handle.set("seed", seed.first, seed.second)
        handle.set("speed", speed)
        handle.set("pixelSize", pixelSize)
        handle.set("gap", gap)
        handle.set("scale", scale)
        handle.set("bloomRadius", bloomRadius)
        handle.set("bloomIntensity", bloomIntensity)
        handle.set("bloomThreshold", bloomThreshold)
        handle.set("randomAmount", randomAmount)
        handle.setColor("bgColor", bgColor)
        handle.setColor("color1", color1)
        handle.setColor("color2", color2)
        handle.setColor("color3", color3)
        handle.setColor("color4", color4)
    }

    fun asComposeShader(): Shader = handle.asComposeShader()

    companion object {
        fun create(): PixelGridShader =
            PixelGridShader(ShaderHandle.create(ShaderSources.PixelGrid))
    }
}

class MeshGradientShader internal constructor(internal val handle: ShaderHandle) {
    var time: Float = 0f
    var resolution: Pair<Float, Float> = 0f to 0f
    var density: Float = 1f
    var seed: Pair<Float, Float> = 0f to 0f
    var speed: Float = 0.4f
    var scale: Float = 1f
    var softness: Float = 0.55f
    var warp: Float = 0.35f
    var color1: Color = Color.White
    var color2: Color = Color.White
    var color3: Color = Color.White
    var color4: Color = Color.White

    fun apply() {
        handle.set("iTime", time)
        handle.set("iResolution", resolution.first, resolution.second)
        handle.set("density", density)
        handle.set("seed", seed.first, seed.second)
        handle.set("speed", speed)
        handle.set("scale", scale)
        handle.set("softness", softness)
        handle.set("warp", warp)
        handle.setColor("color1", color1)
        handle.setColor("color2", color2)
        handle.setColor("color3", color3)
        handle.setColor("color4", color4)
    }

    fun asComposeShader(): Shader = handle.asComposeShader()

    companion object {
        fun create(): MeshGradientShader =
            MeshGradientShader(ShaderHandle.create(ShaderSources.MeshGradient))
    }
}

@Composable
fun rememberGloopShader(): GloopShader = remember { GloopShader.create() }

@Composable
fun rememberPixelGridShader(): PixelGridShader = remember { PixelGridShader.create() }

@Composable
fun rememberMeshGradientShader(): MeshGradientShader =
    remember { MeshGradientShader.create() }
