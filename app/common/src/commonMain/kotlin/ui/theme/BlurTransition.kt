package ui.theme

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class BlurEnter internal constructor(
    internal val animationSpec: FiniteAnimationSpec<Dp>,
    internal val initialRadius: Dp,
)

@Immutable
class BlurExit internal constructor(
    internal val animationSpec: FiniteAnimationSpec<Dp>,
    internal val targetRadius: Dp,
)

fun blurIn(
    animationSpec: FiniteAnimationSpec<Dp> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = Dp.VisibilityThreshold,
    ),
    initialRadius: Dp = 16.dp,
): BlurEnter = BlurEnter(animationSpec, initialRadius)

fun blurOut(
    animationSpec: FiniteAnimationSpec<Dp> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = Dp.VisibilityThreshold,
    ),
    targetRadius: Dp = 16.dp,
): BlurExit = BlurExit(animationSpec, targetRadius)

fun Modifier.animateBlur(
    scope: AnimatedVisibilityScope,
    enter: BlurEnter = blurIn(),
    exit: BlurExit = blurOut(),
): Modifier = composed {
    val blurRadius by scope.transition.animateDp(
        transitionSpec = {
            when {
                EnterExitState.PreEnter isTransitioningTo EnterExitState.Visible ->
                    enter.animationSpec

                EnterExitState.Visible isTransitioningTo EnterExitState.PostExit ->
                    exit.animationSpec

                else -> spring(
                    stiffness = Spring.StiffnessMediumLow,
                    visibilityThreshold = Dp.VisibilityThreshold,
                )
            }
        },
        label = "blur",
    ) { state ->
        when (state) {
            EnterExitState.PreEnter -> enter.initialRadius
            EnterExitState.Visible -> 0.dp
            EnterExitState.PostExit -> exit.targetRadius
        }
    }
    if (blurRadius > 0.dp) blur(blurRadius) else this
}
