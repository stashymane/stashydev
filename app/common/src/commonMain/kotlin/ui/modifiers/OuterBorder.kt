package ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

/** Border drawn outside layout bounds; does not affect measurement. Parents must not clip. */
fun Modifier.outerBorder(width: Dp, color: Color): Modifier = drawWithContent {
    drawContent()
    val stroke = width.toPx()
    drawRect(
        color = color,
        topLeft = Offset(-stroke / 2, -stroke / 2),
        size = Size(size.width + stroke, size.height + stroke),
        style = Stroke(width = stroke),
    )
}
