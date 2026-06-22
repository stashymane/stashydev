package ui.nav

import androidx.compose.animation.*
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import model.Screen
import ui.theme.instantBezier

val navEnterForward = fadeIn(instantBezier()) + scaleIn(instantBezier(), initialScale = 0.95f)
val navExitForward = fadeOut(instantBezier()) + scaleOut(instantBezier(), targetScale = 1.05f)

val navEnterBackward = fadeIn(instantBezier()) + scaleIn(instantBezier(), 1.05f)
val navExitBackward = fadeOut(instantBezier()) + scaleOut(instantBezier(), 0.95f)

val navForward = ContentTransform(navEnterForward, navExitForward)
val navBackward = ContentTransform(navEnterBackward, navExitBackward)

val navGroupEnterRight = fadeIn(instantBezier()) + slideInHorizontally(instantBezier()) { -it }
val navGroupEnterLeft = fadeIn(instantBezier()) + slideInHorizontally(instantBezier()) { it }

val navGroupExitRight = fadeOut(instantBezier()) + slideOutHorizontally(instantBezier()) { it }
val navGroupExitLeft = fadeOut(instantBezier()) + slideOutHorizontally(instantBezier()) { -it }

fun AnimatedContentTransitionScope<Scene<Screen>>.navPopTransition(): ContentTransform =
    navTransition(back = true)

fun AnimatedContentTransitionScope<Scene<Screen>>.navTransition(
    back: Boolean = false
): ContentTransform {
    val initial = initialState.metadata[Screen.Group.MetaKey]
    val target = targetState.metadata[Screen.Group.MetaKey]

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

        else -> if (!back) navForward else navBackward
    }
}

//fun AnimatedContentTransitionScope<Scene<Screen>>.predictiveTransition(@NavigationEvent.SwipeEdge edge: Int): ContentTransform =
//    navTransition {
//        ContentTransform(
//            targetContentEnter = fadeIn(instantBezier()),
//            initialContentExit = fadeOut(instantBezier()) + scaleOut(
//                instantBezier(),
//                targetScale = 0.9f
//            )
//        )
//    }
