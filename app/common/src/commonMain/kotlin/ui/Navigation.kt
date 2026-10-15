package ui

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import dev.stashy.navigation.LocalNavigator
import dev.stashy.navigation.compose.toDecoratedEntries
import dev.stashy.navigation.ui.NavTransitions
import model.Screen
import ui.nav.ResponsiveSceneStrategy

@Composable
fun Navigation() {
    val navigator = LocalNavigator.current
    val transitions = remember { NavTransitions(Screen.Group::compare) }

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                NavDisplay(
                    entries = navigator.state.toDecoratedEntries { destination ->
                        (destination as Screen).provideEntry()
                    },
                    onBack = navigator::goBack,
                    sharedTransitionScope = LocalSharedTransitionScope.current,
                    transitionSpec = transitions.transitionSpec,
                    popTransitionSpec = transitions.popTransitionSpec,
                    predictivePopTransitionSpec = transitions.predictivePopTransitionSpec,
                    sceneStrategies = listOf(
                        remember { ResponsiveSceneStrategy() }
                    ),
                )
            }
        }
    }
}
