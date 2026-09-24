package ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import tiled.toTiledBrush
import kotlin.math.min

@Composable
fun Modifier.dotGridOverlay(
    color: Color = Color.Black,
): Modifier {
    val windowSize = LocalWindowInfo.current.containerSize
    val scale = remember(windowSize) {
        dotGridScaleForResolution(min(windowSize.width, windowSize.height))
    }
    val brush = remember(color, scale) {
        val pixelScale = scale.coerceAtLeast(1)
        val tileSize = 2 * pixelScale
        val bitmap = ImageBitmap(tileSize, tileSize)
        CanvasDrawScope().draw(
            density = Density(1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = Canvas(bitmap),
            size = Size(tileSize.toFloat(), tileSize.toFloat()),
        ) {
            drawRect(color = color, size = Size(pixelScale.toFloat(), pixelScale.toFloat()))
        }
        bitmap.toTiledBrush()
    }
    return background(brush)
}

/** 1080p → 1, 1440p → 2, 4K → 3, 8K → 4 */
internal fun dotGridScaleForResolution(shortSidePx: Int): Int = when {
    shortSidePx < 1440 -> 1
    shortSidePx < 2160 -> 2
    shortSidePx < 4320 -> 3
    else -> 4
}
