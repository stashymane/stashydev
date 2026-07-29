package compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.platform.LocalDensity
import isLowPowerPlatform
import shaders.GloopShader
import shaders.MeshGradientShader
import shaders.PixelGridShader
import shaders.rememberGloopShader
import shaders.rememberMeshGradientShader
import shaders.rememberPixelGridShader
import kotlin.random.Random

@Composable
fun Modifier.glorp(
    seed: Long = remember { Random.nextLong() },
    config: @Composable GloopShader.() -> Unit = {
        bgColor = MaterialTheme.colorScheme.surface
        lineColor = MaterialTheme.colorScheme.surfaceContainerHigh

        speed = 0.5f
        scale = 0.5f
        waveScale = 0.1f
        lineWeight = 0.6f
    }
): Modifier {
    if (isLowPowerPlatform) return Modifier

    val shader = rememberGloopShader()
    val seed = rememberShaderSeed(seed)
    var visible by remember { mutableStateOf(true) }
    val timeState = rememberShaderClock(running = visible)

    config.invoke(shader)

    shader.density = LocalDensity.current.density
    shader.seed = seed

    return this
        .onVisibilityChanged(minFractionVisible = 0f) { visible = it }
        .drawWithCache {
            shader.time = timeState.floatValue
            onDrawBehind { drawRect(ShaderBrush(shader.asComposeShader())) }
        }
}

@Composable
fun Modifier.pixelGrid(
    seed: Long = remember { Random.nextLong() },
    config: @Composable PixelGridShader.() -> Unit = {
        bgColor = MaterialTheme.colorScheme.surface
        color1 = bgColor
        color2 = lerp(MaterialTheme.colorScheme.surfaceVariant, bgColor, 0.5f)
        color3 = MaterialTheme.colorScheme.primaryContainer
        color4 = MaterialTheme.colorScheme.onSurface

        scale = 0.6f
        bloomRadius = 60f
        bloomIntensity = 0.025f
        bloomThreshold = 0.3f
        randomAmount = 0.15f
    }
): Modifier {
    if (isLowPowerPlatform) return Modifier

    val shader = rememberPixelGridShader()
    val seed = rememberShaderSeed(seed)
    var visible by remember { mutableStateOf(true) }
    val timeState = rememberShaderClock(running = visible)

    config.invoke(shader)

    shader.density = LocalDensity.current.density
    shader.seed = seed

    return this
        .onVisibilityChanged(minFractionVisible = 0f) { visible = it }
        .drawWithCache {
            shader.time = timeState.floatValue
            shader.resolution = size.width to size.height
            onDrawBehind { drawRect(ShaderBrush(shader.asComposeShader())) }
        }
}

@Composable
fun Modifier.meshGradient(
    seed: Long = remember { Random.nextLong() },
    config: @Composable MeshGradientShader.() -> Unit = {
        color1 = MaterialTheme.colorScheme.primary
        color2 = MaterialTheme.colorScheme.primaryContainer
        color3 = MaterialTheme.colorScheme.surfaceContainerLow
        color4 = MaterialTheme.colorScheme.background

        speed = 0.7f
        warp = 1f
    }
): Modifier {
    if (isLowPowerPlatform) return Modifier

    val shader = rememberMeshGradientShader()
    val seed = rememberShaderSeed(seed)
    var visible by remember { mutableStateOf(true) }
    val timeState = rememberShaderClock(running = visible)

    config.invoke(shader)

    shader.density = LocalDensity.current.density
    shader.seed = seed

    return this
        .onVisibilityChanged(minFractionVisible = 0f) { visible = it }
        .drawWithCache {
            shader.time = timeState.floatValue
            shader.resolution = size.width to size.height
            onDrawBehind { drawRect(ShaderBrush(shader.asComposeShader())) }
        }
}
