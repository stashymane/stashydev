package theme

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import org.jetbrains.skia.RuntimeShaderBuilder

fun Modifier.glorp(): Modifier = composed {
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis { value = it / 1000f }
        }
    }

    this.drawWithCache {
        val shader = RuntimeShaderBuilder(Shaders.Pattern).apply {
            uniform("iTime", time)
            uniform("iResolution", size.width, size.height)
            uniform("Warp2X", 0.1f)
            uniform("Warp2Y", 0f)
            uniform("WarpSpeedX", 0f)
            uniform("WarpSpeedY", 0.1f)
            uniform("RingStrength", 2f)
        }.makeShader()
        val brush = ShaderBrush(shader)

        onDrawBehind {
            drawRect(brush)
        }
    }
}
