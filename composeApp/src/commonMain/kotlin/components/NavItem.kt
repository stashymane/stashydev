package components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import org.jetbrains.compose.ui.tooling.preview.Preview

private val maskGradient =
    Brush.verticalGradient(
        0f to Color.White,
        0.1f to Color.White.copy(alpha = 1f),
        0.2f to Color.White.copy(alpha = 0.9f),
        0.4f to Color.White.copy(alpha = 0.6f),
        0.6f to Color.White.copy(alpha = 0f)
    )

@Composable
fun NavItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    background: @Composable (Modifier) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()

    val backgroundAlpha = animateFloatAsState(if (hovered) 1f else 0f)

    Box(modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            onClick = onClick,
            interactionSource = interactionSource
        ) {
            Box(Modifier.matchParentSize().graphicsLayer {
                alpha = backgroundAlpha.value
                compositingStrategy = CompositingStrategy.Offscreen
            }.drawWithContent {
                drawContent()
                drawRect(maskGradient, blendMode = BlendMode.DstOut)
            }) {
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

@Preview
@Composable
private fun NavItemPreview() {
    NavItem(onClick = {}, icon = Icons.OutlineLarge.CaptivePortal, text = "Test", background = {})
}
