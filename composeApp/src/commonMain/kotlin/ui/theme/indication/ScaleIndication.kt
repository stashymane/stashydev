package ui.theme.indication

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private class ScaleIndicationNode(
    private val interactionSource: InteractionSource,
    private val pressedScale: Float,
    private val pivot: Boolean,
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {
    var currentPressPosition: Offset = Offset.Zero
    val animatedScalePercent = Animatable(1f)

    private suspend fun animateToPressed(pressPosition: Offset) {
        val motionScheme = currentValueOf(MaterialTheme.LocalMaterialTheme).motionScheme

        currentPressPosition = pressPosition
        animatedScalePercent.animateTo(pressedScale, motionScheme.fastSpatialSpec())
    }

    private suspend fun animateToResting() {
        val motionScheme = currentValueOf(MaterialTheme.LocalMaterialTheme).motionScheme
        animatedScalePercent.animateTo(1f, motionScheme.fastSpatialSpec())
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
    val pressedScale: Float = 0.95f,
    val pivot: Boolean = false,
) : IndicationNodeFactory {
    companion object {
        val Default: ScaleIndication = ScaleIndication()
    }

    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleIndicationNode(interactionSource, pressedScale, pivot)
    }

    override fun hashCode(): Int = -1
    override fun equals(other: Any?): Boolean = other === this
}

fun scale(
    pressedScale: Float? = null,
    pivot: Boolean? = null
): IndicationNodeFactory {
    return if (pressedScale == null && pivot == null) {
        ScaleIndication.Default
    } else {
        ScaleIndication(
            pressedScale ?: 0.95f,
            pivot ?: false
        )
    }
}
