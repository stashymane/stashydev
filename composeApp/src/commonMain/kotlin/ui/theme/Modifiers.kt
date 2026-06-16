package ui.theme

import AppShader
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import gloop

@Composable
fun Modifier.glorp(): Modifier {
    val shader = AppShader.gloop()

    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis { value = it / 1000f }
        }
    }

    shader.apply {
        set("Warp2X", 0.1f)
        set("Warp2Y", 0f)
        set("WarpSpeedX", 0f)
        set("WarpSpeedY", 0.1f)
        set("RingStrength", 2f)
    }

    return this.drawWithCache {
        shader.apply {
            set("iTime", time)
            set("iResolution", size.width, size.height)
        }

        val brush = ShaderBrush(shader.asComposeShader())

        onDrawBehind {
            drawRect(brush)
        }
    }
}
