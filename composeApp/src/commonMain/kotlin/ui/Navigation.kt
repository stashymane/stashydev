package ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import model.Screen
import ui.nav.decorators.BackgroundEntryDecorator
import ui.nav.decorators.NavigationEntryDecorator
import ui.nav.decorators.ResponsiveNavEntryDecorator
import ui.nav.navPopTransition
import ui.nav.navTransition
import ui.nav.scenes.PageSceneStrategy
import ui.screens.BackgroundScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    val backStack = LocalBackStack.current

    BackgroundScreen(Modifier.fillMaxSize())

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                NavDisplay(
                    backStack.backStack,
                    onBack = backStack::removeLast,
                    sharedTransitionScope = LocalSharedTransitionScope.current,
                    transitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
                    popTransitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navPopTransition,
                    sceneStrategies = listOf(
                        remember { PageSceneStrategy() }
                    ),
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        remember { ResponsiveNavEntryDecorator() },
                        remember { BackgroundEntryDecorator() },
                        remember { NavigationEntryDecorator() }
                    ),
                    entryProvider = Screen::provideEntry
                )
            }
        }
    }
}
