package ui.components.nav

import androidx.compose.animation.core.EaseInOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.stashy.home.Res
import dev.stashy.home.block_projects_1k
import icons.Icons
import icons.outlinelarge.CaptivePortal
import org.jetbrains.compose.resources.imageResource
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.easeVerticalGradient
import ui.theme.indication.scale

@Composable
fun NavBlock(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    background: @Composable () -> Unit
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
        Box(Modifier.drawWithCache {
            val overlayGradient =
                Brush.easeVerticalGradient(
                    surfaceColor,
                    surfaceColor.copy(alpha = 0f),
                    10,
                    end = 1f,
                    easing = EaseInOut
                )

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
