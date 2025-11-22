package ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import ui.theme.exponentialVerticalGradient

private val maskGradient =
    Brush.exponentialVerticalGradient(
        Color.White.copy(alpha = 0.75f),
        Color.White.copy(alpha = 0f),
        20,
        curve = 0.4f
    )

@Composable
fun ScreenBackground(drawable: DrawableResource, modifier: Modifier = Modifier) {
    Box(modifier.graphicsLayer {
        compositingStrategy = CompositingStrategy.Offscreen
    }.drawWithContent {
        drawContent()
        drawRect(maskGradient, blendMode = BlendMode.DstIn)
    }) {
        Image(
            imageResource(drawable),
            null,
            Modifier.matchParentSize().blur(16.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )
    }
}
