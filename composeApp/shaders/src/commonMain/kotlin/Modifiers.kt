import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp

@Composable
fun Modifier.glorp(
    bgColor: Color = MaterialTheme.colorScheme.surface,
    lineColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        .compositeOver(MaterialTheme.colorScheme.surface),
    speed: Float = 0.25f,
    waveScale: Float = 0.5f,
    lineWeight: Float = 0.16f,
    seed: Long = 0L,
): Modifier {
    val shader = rememberGloopShader()

    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis { value = it / 1000f }
        }
    }

    val seedX = ((seed xor 0x9E3779B9) % 10000).toFloat()
    val seedY = ((seed xor 0x517CC1B7) % 10000).toFloat()

    return this.drawBehind {
        shader.time = time
        shader.resolution = size.width to size.height
        shader.density = density
        shader.seed = seedX to seedY
        shader.speed = speed
        shader.waveScale = waveScale
        shader.lineWeight = lineWeight
        shader.bgColor = bgColor
        shader.lineColor = lineColor
        shader.apply()

        drawRect(ShaderBrush(shader.asComposeShader()))
    }
}

@Composable
fun Modifier.pixelGrid(
    bgColor: Color = MaterialTheme.colorScheme.surface,
    fgColor: Color = MaterialTheme.colorScheme.onSurface,
    c2: Color = lerp(MaterialTheme.colorScheme.surfaceVariant, bgColor, 0.5f),
    c3: Color = MaterialTheme.colorScheme.primaryContainer,
    speed: Float = 1f,
    pixelSize: Float = 8f,
    gap: Float = 2f,
    bloomRadius: Float = 80f,
    bloomIntensity: Float = 0.03f,
    bloomThreshold: Float = 0.3f,
    randomAmount: Float = 0.2f,
    seed: Long = 0L,
): Modifier {
    val shader = rememberPixelGridShader()

    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis { value = it / 1000f }
        }
    }

    val seedX = ((seed xor 0x9E3779B9) % 10000).toFloat()
    val seedY = ((seed xor 0x517CC1B7) % 10000).toFloat()

    return this.drawBehind {
        shader.time = time
        shader.resolution = size.width to size.height
        shader.density = density
        shader.seed = seedX to seedY
        shader.speed = speed
        shader.pixelSize = pixelSize
        shader.gap = gap
        shader.bloomRadius = bloomRadius
        shader.bloomIntensity = bloomIntensity
        shader.bloomThreshold = bloomThreshold
        shader.randomAmount = randomAmount
        shader.bgColor = bgColor
        shader.color1 = bgColor
        shader.color2 = c2
        shader.color3 = c3
        shader.color4 = fgColor
        shader.apply()

        drawRect(ShaderBrush(shader.asComposeShader()))
    }
}

@Composable
fun Modifier.meshGradient(
    color1: Color = MaterialTheme.colorScheme.primary,
    color2: Color = MaterialTheme.colorScheme.primaryContainer,
    color3: Color = MaterialTheme.colorScheme.surface,
    color4: Color = MaterialTheme.colorScheme.background,
    speed: Float = 0.6f,
    scale: Float = 1f,
    softness: Float = 0.55f,
    warp: Float = 0.35f,
    seed: Long = 0L,
): Modifier {
    val shader = rememberMeshGradientShader()

    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis { value = it / 1000f }
        }
    }

    val seedX = ((seed xor 0x9E3779B9) % 10000).toFloat()
    val seedY = ((seed xor 0x517CC1B7) % 10000).toFloat()

    return this.drawBehind {
        shader.time = time
        shader.resolution = size.width to size.height
        shader.density = density
        shader.seed = seedX to seedY
        shader.speed = speed
        shader.scale = scale
        shader.softness = softness
        shader.warp = warp
        shader.color1 = color1
        shader.color2 = color2
        shader.color3 = color3
        shader.color4 = color4
        shader.apply()

        drawRect(ShaderBrush(shader.asComposeShader()))
    }
}
