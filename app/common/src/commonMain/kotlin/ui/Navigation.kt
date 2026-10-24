package ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import appEntryProvider
import dev.stashy.navigation.compose.rememberEntryStateNavEntryDecorator
import dev.stashy.navigation.compose.rememberNavigationState
import dev.stashy.navigation.compose.rememberNavigator
import dev.stashy.navigation.ui.rememberNavigatorSceneDecorator
import model.Screen
import model.TopLevelRoutes
import ui.nav.ResponsiveSceneStrategy
import ui.nav.navBackTransition
import ui.nav.navPredictiveTransition
import ui.nav.navTransition

@Composable
fun Navigation() {
    val state = rememberNavigationState(
        startRoute = Screen.Home,
        topLevelRoutes = TopLevelRoutes,
    )
    val navigator = rememberNavigator<Screen>(state)

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                NavDisplay(
                    backStack = state.backStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberEntryStateNavEntryDecorator(state),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = remember { appEntryProvider() },
                    onBack = navigator::goBack,
                    sharedTransitionScope = LocalSharedTransitionScope.current,
                    transitionSpec = AnimatedContentTransitionScope<Scene<NavKey>>::navTransition,
                    popTransitionSpec = AnimatedContentTransitionScope<Scene<NavKey>>::navBackTransition,
                    predictivePopTransitionSpec = AnimatedContentTransitionScope<Scene<NavKey>>::navPredictiveTransition,
                    sceneStrategies = listOf(
                        remember { ResponsiveSceneStrategy() }
                    ),
                    sceneDecoratorStrategies = listOf(
                        rememberNavigatorSceneDecorator(navigator),
                    ),
                )
            }
        }
    }
}
