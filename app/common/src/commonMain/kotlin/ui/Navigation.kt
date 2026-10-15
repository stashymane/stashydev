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
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import dev.stashy.navigation.LocalNavigator
import dev.stashy.navigation.NavDestination
import dev.stashy.navigation.compose.toDecoratedEntries
import model.Screen
import ui.nav.ResponsiveSceneStrategy
import ui.nav.navPopTransition
import ui.nav.navTransition

@Composable
fun Navigation() {
    val navigator = LocalNavigator.current

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
                    transitionSpec = AnimatedContentTransitionScope<Scene<NavDestination>>::navTransition,
                    popTransitionSpec = AnimatedContentTransitionScope<Scene<NavDestination>>::navPopTransition,
                    sceneStrategies = listOf(
                        remember { ResponsiveSceneStrategy() }
                    ),
                )
            }
        }
    }
}
