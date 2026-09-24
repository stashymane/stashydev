package ui.theme.indication

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EdgeIndicationNode(
    private val interactionSource: InteractionSource,
    private val offsetDp: Dp = 15.dp,
    private val lengthDp: Dp = 20.dp,
    private val color: Color = Color.LightGray,
    private val backgroundColor: Color = Color.Black.copy(alpha = 0.2f),
) : Modifier.Node(), DrawModifierNode {
    val progress = Animatable(0f)

    private suspend fun hovered() {
        progress.animateTo(1f)
    }

    private suspend fun unhovered() {
        progress.animateTo(0f)
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> hovered()
                    is HoverInteraction.Exit -> unhovered()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val offset = offsetDp.toPx()
        val length = lengthDp.toPx()

        val currentOffset = offset * progress.value
        val alpha = progress.value
        val strokeWidth = 1f
        val halfStroke = strokeWidth / 2f

        val rect = Rect(
            offset = Offset(-currentOffset, -currentOffset),
            size = Size(size.width + (currentOffset * 2), size.height + (currentOffset * 2))
        )

        drawRect(color = backgroundColor, topLeft = rect.topLeft, size = rect.size, alpha = alpha)

        drawContent()

        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)

        fun drawEdge(start: Offset, end: Offset, inset: Offset) {
            val adjustedStart = start + inset
            val adjustedEnd = end + inset

            val direction = (adjustedEnd - adjustedStart) / (adjustedEnd - adjustedStart).getDistance()
            val segment = direction * length

            drawLine(color, adjustedStart, adjustedStart + segment, alpha = alpha, strokeWidth = strokeWidth)
            drawLine(color, adjustedEnd - segment, adjustedEnd, alpha = alpha, strokeWidth = strokeWidth)

            drawLine(
                color.copy(alpha = 0.1f),
                adjustedStart + segment,
                adjustedEnd - segment,
                alpha = alpha,
                pathEffect = dashEffect,
                strokeWidth = strokeWidth
            )
        }

        drawEdge(rect.topLeft, rect.topRight, Offset(0f, halfStroke))       // Top (move down)
        drawEdge(rect.bottomLeft, rect.bottomRight, Offset(0f, -halfStroke)) // Bottom (move up)
        drawEdge(rect.topLeft, rect.bottomLeft, Offset(halfStroke, 0f))     // Left (move right)
        drawEdge(rect.topRight, rect.bottomRight, Offset(-halfStroke, 0f))   // Right (move left)
    }
}

object EdgeIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return EdgeIndicationNode(interactionSource)
    }

    override fun hashCode(): Int = -1
    override fun equals(other: Any?): Boolean = other === this
}
