package ui.nav

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent.Companion.EDGE_LEFT
import androidx.navigationevent.NavigationEvent.Companion.EDGE_RIGHT
import androidx.navigationevent.NavigationEvent.SwipeEdge
import dev.stashy.standard.ui.animation.tweenInstantOut

private typealias NavTransitionScope = AnimatedContentTransitionScope<Scene<NavKey>>

private val Animation: FiniteAnimationSpec<Float> = tweenInstantOut()
private val OffsetAnimation: FiniteAnimationSpec<IntOffset> = tweenInstantOut()

private const val InitialScale = 0.95f
private const val TargetScale = 1.05f
private const val PredictiveScale = 0.95f
private const val PredictiveOffset = 1f / 8f
private const val RootOffset = 1f / 16f

internal object CrossRootIndexKey : NavMetadataKey<Int>

internal fun crossRootMetadata(index: Int): Map<String, Any> = metadata {
    put(CrossRootIndexKey, index)
}

private fun navEnter(initialScale: Float): EnterTransition =
    fadeIn(Animation) + scaleIn(Animation, initialScale = initialScale)

private fun navExit(targetScale: Float): ExitTransition =
    fadeOut(Animation) + scaleOut(Animation, targetScale = targetScale)

private fun navRootEnter(offsetFraction: Float): EnterTransition =
    fadeIn(Animation) + slideInHorizontally(OffsetAnimation) { (it * offsetFraction).toInt() }

private fun navRootExit(offsetFraction: Float): ExitTransition =
    fadeOut(Animation) + slideOutHorizontally(OffsetAnimation) { (it * offsetFraction).toInt() }

private val NavForward = ContentTransform(
    navEnter(InitialScale),
    navExit(TargetScale),
)

private val NavBackward = ContentTransform(
    navEnter(TargetScale),
    navExit(InitialScale),
)

/** Moving to a later top-level route: enter from the right, exit to the left. */
private val NavRootForward = ContentTransform(
    navRootEnter(RootOffset),
    navRootExit(-RootOffset),
)

/** Moving to an earlier top-level route: enter from the left, exit to the right. */
private val NavRootBackward = ContentTransform(
    navRootEnter(-RootOffset),
    navRootExit(RootOffset),
)

private fun navPredictive(edge: Int): ContentTransform {
    val signedOffset = when (edge) {
        EDGE_LEFT -> PredictiveOffset
        EDGE_RIGHT -> -PredictiveOffset
        else -> 0f
    }

    return ContentTransform(
        fadeIn(Animation),
        navExit(PredictiveScale) +
                slideOutHorizontally(OffsetAnimation) { (it * signedOffset).toInt() },
    )
}

/**
 * Forward/pop transition within a root, or a directional slide when switching roots.
 */
fun NavTransitionScope.navTransition(back: Boolean = false): ContentTransform {
    val direction = crossRootDirection()

    return when {
        direction > 0 -> NavRootForward
        direction < 0 -> NavRootBackward
        else -> if (back) NavBackward else NavForward
    }
}

fun NavTransitionScope.navBackTransition(): ContentTransform = navTransition(true)

/**
 * Predictive-back transform, retaining directional slides for root switches.
 */
fun NavTransitionScope.navPredictiveTransition(@SwipeEdge edge: Int): ContentTransform {
    val direction = crossRootDirection()

    return when {
        direction > 0 -> NavRootForward
        direction < 0 -> NavRootBackward
        else -> navPredictive(edge)
    }
}

private fun NavTransitionScope.crossRootDirection(): Int {
    val from = initialState.rootIndex() ?: return 0
    val to = targetState.rootIndex() ?: return 0

    if (from == 0 || to == 0) return 0
    return to.compareTo(from)
}

/**
 * Resolves the root that owns this scene from its entry ancestry.
 * Overlays do not participate in root switching.
 */
private fun Scene<*>.rootIndex(): Int? {
    if (this is OverlayScene<*>) return null
    return entries.asReversed().firstNotNullOfOrNull { it.metadata[CrossRootIndexKey] }
}
