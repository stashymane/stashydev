package ui

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import appNavGraph
import dev.stashy.navigation.Navigator
import dev.stashy.navigation.compose.SyncBrowserHistory
import dev.stashy.navigation.compose.rememberEntryStateNavEntryDecorator
import dev.stashy.navigation.compose.rememberNavigationState
import dev.stashy.navigation.ui.NavTransitions
import dev.stashy.navigation.ui.rememberNavigatorSceneDecorator
import model.Screen
import ui.nav.ResponsiveSceneStrategy

@Composable
fun Navigation() {
    val graph = remember { appNavGraph() }
    val state = rememberNavigationState(startRoute = Screen.Home, graph = graph)
    val navigator = remember(state, graph) { Navigator(state, graph) }
    SyncBrowserHistory<Screen>(navigator)

    val transitions = remember {
        NavTransitions(
            comparePeerIndex = { from, to ->
                if (from == 0 || to == 0) 0 else from.compareTo(to)
            }
        )
    }

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                NavDisplay(
                    backStack = state.entries,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberEntryStateNavEntryDecorator(state),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = graph.entryProvider,
                    onBack = navigator::goBack,
                    sharedTransitionScope = LocalSharedTransitionScope.current,
                    transitionSpec = transitions.transitionSpec,
                    popTransitionSpec = transitions.popTransitionSpec,
                    predictivePopTransitionSpec = transitions.predictivePopTransitionSpec,
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
