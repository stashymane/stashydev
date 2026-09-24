package compose

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import isLowPowerPlatform

@Composable
internal fun rememberShaderClock(running: Boolean): MutableFloatState {
    val inspection = LocalInspectionMode.current
    val frozen = inspection || isLowPowerPlatform
    val time = remember { mutableFloatStateOf(if (frozen) 1.2f else 0f) }
    LaunchedEffect(running, frozen) {
        if (frozen || !running) return@LaunchedEffect
        while (true) {
            withInfiniteAnimationFrameMillis { time.floatValue = it / 1000f }
        }
    }
    return time
}

@Composable
internal fun rememberShaderSeed(seed: Long): Pair<Float, Float> {
    return remember(seed) {
        val x = ((seed xor 0x9E3779B9) % 10000).toFloat()
        val y = ((seed xor 0x517CC1B7) % 10000).toFloat()
        x to y
    }
}
