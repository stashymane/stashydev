package ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import model.Screen
import ui.locals.LocalBackStack
import ui.locals.LocalSharedTransitionScope
import ui.nav.navTransition
import ui.screens.*

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    val backStack = LocalBackStack.current

    BackgroundScreen(Modifier.fillMaxSize())

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            NavDisplay(
                backStack.backStack,
                onBack = backStack::removeLast,
                transitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
                popTransitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
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
