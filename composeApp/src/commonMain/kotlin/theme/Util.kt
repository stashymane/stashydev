package theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun exponentialGradient(color: Color, steps: Int, end: Float = 1f): List<Pair<Float, Color>> = buildList {
    for (i in 0 until steps) {
        val pos = i / steps.toFloat() * end
        val normalizedPos = pos / end
        val alpha = (1f - normalizedPos * normalizedPos).coerceAtLeast(0f)
        add(pos to color.copy(alpha = alpha))
    }
}

fun Brush.Companion.exponentialVerticalGradient(color: Color, steps: Int, end: Float = 1f) =
    Brush.verticalGradient(*exponentialGradient(color, steps, end).toTypedArray())
