package components.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AnimatedLaunch(
    values: ClosedFloatingPointRange<Float>,
    modifier: GraphicsLayerScope.(Float) -> Unit,
    spec: AnimationSpec<Float> = tween(1000),
    content: @Composable (Modifier) -> Unit
) {
    val animation = remember { Animatable(values.start) }
    val graphicsModifier = Modifier.graphicsLayer { modifier(animation.value) }

    LaunchedEffect(Unit) {
        animation.animateTo(values.endInclusive, spec)
    }

    content(graphicsModifier)
}
