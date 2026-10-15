package ui.components.nav

import androidx.compose.animation.core.EaseInOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.stashy.home.Res
import dev.stashy.home.block_projects_1k
import dev.stashy.standard.ui.gradients.easedVerticalGradient
import dev.stashy.standard.ui.gradients.stop
import dev.stashy.standard.ui.gradients.toEasingFunction
import icons.Icons
import icons.outlinelarge.CaptivePortal
import org.jetbrains.compose.resources.imageResource
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.indication.scale

@Composable
fun NavBlock(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    background: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        modifier.sizeIn(minHeight = 300.dp)
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            )
            .pointerHoverIcon(PointerIcon.Hand)
            .indication(interactionSource, scale())
            .indication(interactionSource, ripple())
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            .background(surfaceColor)
    ) {
        Box(Modifier.graphicsLayer {
            compositingStrategy = Offscreen
        }.drawWithCache {
            val overlayGradient = Brush.easedVerticalGradient(steps = 10) {
                easing = EaseInOut.toEasingFunction()
                stop(surfaceColor)
                stop(surfaceColor.copy(alpha = 0f))
            }

            onDrawWithContent {
                drawContent()
                drawRect(overlayGradient)
            }
        }.matchParentSize()) {
            background()
        }

        NavTitle(icon, text, Modifier.padding(16.dp))
    }
}

@ComponentPreview
@Composable
private fun NavBlockPreview() = PreviewHost {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        NavBlock(
            onClick = {},
            icon = Icons.OutlineLarge.CaptivePortal,
            text = "Test",
            background = {})

        NavBlock(
            onClick = {},
            icon = Icons.OutlineLarge.CaptivePortal,
            text = "Test",
            background = {
                Image(
                    imageResource(Res.drawable.block_projects_1k),
                    null,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            })
    }
}
