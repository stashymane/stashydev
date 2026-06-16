package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.dottedLine(
    color: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    width: Dp = Dp.Hairline,
    intervals: FloatArray = floatArrayOf(10f, 10f)
) = drawBehind {
    drawLine(
        color = color,
        start = Offset(0f, size.height / 2 - 0.5f),
        end = Offset(size.width, size.height / 2 - 0.5f),
        strokeWidth = (width).toPx(),
        pathEffect = PathEffect.dashPathEffect(intervals, 0f)
    )
}
