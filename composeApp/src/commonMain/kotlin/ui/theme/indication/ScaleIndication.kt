package ui.theme.indication

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ui.theme.instantBezier

private class ScaleIndicationNode(
    private val interactionSource: InteractionSource,
    private val targetScale: Float,
    private val pivot: Boolean,
) : Modifier.Node(), DrawModifierNode {
    var currentPressPosition: Offset = Offset.Zero
    val animatedScalePercent = Animatable(1f)

    private suspend fun animateToPressed(pressPosition: Offset) {
        currentPressPosition = pressPosition
        animatedScalePercent.animateTo(targetScale, instantBezier())
    }

    private suspend fun animateToResting() {
        animatedScalePercent.animateTo(1f, instantBezier())
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                    is PressInteraction.Release, is PressInteraction.Cancel, is HoverInteraction.Exit -> animateToResting()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(
            scale = animatedScalePercent.value,
            pivot = if (pivot) currentPressPosition else center
        ) {
            this@draw.drawContent()
        }
    }
}

class ScaleIndication(
    val targetScale: Float = 0.95f,
    val pivot: Boolean = false,
) : IndicationNodeFactory {
    companion object {
        val Default: ScaleIndication = ScaleIndication()
    }

    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleIndicationNode(interactionSource, targetScale, pivot)
    }

    override fun hashCode(): Int = -1
    override fun equals(other: Any?): Boolean = other === this
}
