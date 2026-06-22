package ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import model.Screen
import ui.locals.LocalBackStack
import ui.locals.LocalSharedTransitionScope
import ui.nav.decorators.ResponsiveNavEntryDecorator
import ui.nav.decorators.TopBarEntryDecorator
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
            NavDisplay(
                backStack.backStack,
                onBack = backStack::removeLast,
                transitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
                popTransitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
                sceneStrategies = listOf(
                    remember { PageSceneStrategy() }
                ),
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    remember { ResponsiveNavEntryDecorator() },
                    remember { TopBarEntryDecorator() },
                ),
                entryProvider = Screen::provideEntry
            )
        }
    }
}
