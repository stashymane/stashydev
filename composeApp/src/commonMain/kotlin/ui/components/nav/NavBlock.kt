package ui.components.nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outlinelarge.CaptivePortal
import ui.preview.ComponentPreview
import ui.theme.exponentialVerticalGradient
import ui.theme.indication.ScaleIndication

private val maskGradient =
    Brush.exponentialVerticalGradient(
        Color.White,
        Color.White.copy(alpha = 0f),
        10,
        curve = 1.25f
    )

private val rippleAlpha = RippleAlpha(
    pressedAlpha = 0.1f,
    focusedAlpha = 0.1f,
    draggedAlpha = 0.16f,
    hoveredAlpha = 0f,
)

@Composable
fun NavBlock(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    background: @Composable (Modifier) -> Unit
) {
    CompositionLocalProvider() {
        val interactionSource = remember { MutableInteractionSource() }
        val hovered by interactionSource.collectIsHoveredAsState()
        val focused by interactionSource.collectIsFocusedAsState()
        val pressed by interactionSource.collectIsPressedAsState()

        val backgroundAlpha by animateFloatAsState(if (pressed) 0.5f else if (hovered || focused) 1f else 0f)
        val backgroundColor by animateColorAsState(if (hovered || focused || pressed) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surface)

        Box(
            modifier.sizeIn(minHeight = 300.dp)
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = null
                )
                .indication(interactionSource, ScaleIndication.Default)
                .indication(interactionSource, ripple())
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                .background(backgroundColor)
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

            ProvideTextStyle(MaterialTheme.typography.headlineLarge) {
                Column {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(icon, null, Modifier.size(42.dp))

                        Text(text)
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun NavBlockPreview() {
    NavBlock(
        onClick = {},
        icon = Icons.OutlineLarge.CaptivePortal,
        text = "Test",
        background = {})
}
