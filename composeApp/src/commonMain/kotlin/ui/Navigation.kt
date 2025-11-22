package ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import model.Screen
import ui.locals.LocalBackStack
import ui.locals.LocalSharedTransitionScope
import ui.screens.AboutScreen
import ui.screens.HomeScreen
import ui.screens.MediaScreen
import ui.screens.ProjectsScreen
import ui.theme.fastBezier

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    val backStack = LocalBackStack.current

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            NavDisplay(
                backStack.backStack,
                onBack = backStack::removeLast,
                transitionSpec = {
                    fadeIn(fastBezier()) + scaleIn(
                        fastBezier(),
                        initialScale = 0.9f
                    ) togetherWith fadeOut(fastBezier()) + scaleOut(
                        fastBezier(), targetScale = 1.1f
                    )
                },
                popTransitionSpec = {
                    fadeIn(fastBezier()) + scaleIn(
                        fastBezier(),
                        initialScale = 1.1f
                    ) togetherWith fadeOut(fastBezier()) + scaleOut(
                        fastBezier(), targetScale = 0.9f
                    )
                },
                entryProvider = { key ->
                    when (key) {
                        is Screen.Home -> NavEntry(key) {
                            HomeScreen()
                        }

                        is Screen.Projects -> NavEntry(key) {
                            ProjectsScreen()
                        }

                        is Screen.Media -> NavEntry(key) {
                            MediaScreen()
                        }

                        is Screen.About -> NavEntry(key) {
                            AboutScreen()
                        }
                    }
                })
        }
    }
}
