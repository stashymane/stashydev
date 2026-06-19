package ui.components.nav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outlinelarge.CaptivePortal
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.exponentialVerticalGradient
import ui.theme.indication.scale
import ui.theme.navTitleSharedElement

private val maskGradient =
    Brush.exponentialVerticalGradient(
        Color.White,
        Color.White.copy(alpha = 0f),
        10,
        curve = 1.25f
    )

private val overlayGradient =
    Brush.exponentialVerticalGradient(
        Color.White.copy(alpha = 0.02f),
        Color.White.copy(alpha = 0f),
        10,
        curve = 1.25f
    )

@Composable
fun NavBlock(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    background: @Composable (Modifier) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val pressed by interactionSource.collectIsPressedAsState()

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.5f else if (hovered || focused) 1f else 0f,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
    )

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
            .background(MaterialTheme.colorScheme.surface)
            .drawWithContent {
                drawContent()
                drawRect(overlayGradient)
            }
    ) {
        Box(Modifier.graphicsLayer {
            alpha = backgroundAlpha
            compositingStrategy = CompositingStrategy.Offscreen
            blendMode = BlendMode.Screen
        }.drawWithContent {
            drawContent()
            drawRect(maskGradient, blendMode = BlendMode.DstOut)
        }.matchParentSize()) {
            background(Modifier.matchParentSize())
        }

        NavTitle(icon, text, Modifier.padding(16.dp).navTitleSharedElement("title-$text"))
    }
}

@ComponentPreview
@Composable
private fun NavBlockPreview() = PreviewHost {
    NavBlock(
        onClick = {},
        icon = Icons.OutlineLarge.CaptivePortal,
        text = "Test",
        background = {})
}
