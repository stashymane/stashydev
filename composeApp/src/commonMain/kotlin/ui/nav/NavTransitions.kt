package ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent
import model.Screen
import ui.theme.instantBezier

val navEnter = fadeIn(instantBezier())
val navExit = fadeOut(instantBezier(delayMillis = 300))
val navDefault = ContentTransform(
    targetContentEnter = navEnter,
    initialContentExit = navExit
)

val navGroupEnterRight =
    fadeIn(instantBezier(durationMillis = 100)) + slideInHorizontally(
        instantBezier()
    ) { -it }
val navGroupEnterLeft =
    fadeIn(instantBezier(durationMillis = 100)) + slideInHorizontally(
        instantBezier()
    ) { it }

val navGroupExitRight =
    fadeOut(instantBezier(delayMillis = 100)) + slideOutHorizontally(
        instantBezier()
    ) { it }
val navGroupExitLeft =
    fadeOut(instantBezier(delayMillis = 100)) + slideOutHorizontally(
        instantBezier()
    ) { -it }

fun AnimatedContentTransitionScope<Scene<Screen>>.navTransition(default: () -> ContentTransform = { navDefault }): ContentTransform {
    val initial =
        initialState.metadata[Screen.Group.META_KEY] as Screen.Group?
    val target =
        targetState.metadata[Screen.Group.META_KEY] as Screen.Group?

    val direction = initial?.towards(target) ?: 0

    return when {
        direction > 0 -> ContentTransform(
            targetContentEnter = navGroupEnterRight,
            initialContentExit = navGroupExitRight
        )

        direction < 0 -> ContentTransform(
            targetContentEnter = navGroupEnterLeft,
            initialContentExit = navGroupExitLeft
        )

        else -> default()
    }
}

fun AnimatedContentTransitionScope<Scene<Screen>>.predictiveTransition(@NavigationEvent.SwipeEdge edge: Int): ContentTransform =
    navTransition {
        ContentTransform(
            targetContentEnter = fadeIn(instantBezier()),
            initialContentExit = fadeOut(instantBezier()) + scaleOut(
                instantBezier(),
                targetScale = 0.9f
            )
        )
    }
