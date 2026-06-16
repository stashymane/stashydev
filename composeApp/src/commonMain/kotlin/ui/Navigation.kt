package ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import model.Screen
import ui.components.SiteFooter
import ui.components.SiteHeader
import ui.locals.LocalBackStack
import ui.locals.LocalSharedTransitionScope
import ui.nav.navTransition
import ui.nav.scenes.PageSceneStrategy
import ui.screens.*
import ui.screens.generic.ScreenContainer

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    val backStack = LocalBackStack.current
    val strategies = listOf(PageSceneStrategy<Screen>())

    BackgroundScreen(Modifier.fillMaxSize())

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ScreenContainer {
                    SiteHeader(
                        showLinks = backStack.isEmpty()
                    )
                }

                NavDisplay(
                    backStack.backStack,
                    onBack = backStack::removeLast,
                    transitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
                    popTransitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
                    sceneStrategies = strategies,
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

                SiteFooter()
            }
        }
    }
}
