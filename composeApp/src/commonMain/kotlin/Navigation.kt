import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import locals.LocalBackStack
import locals.LocalSharedTransitionScope
import screens.HomeScreen
import screens.ProjectsScreen
import screens.Screen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(contentPadding: PaddingValues) {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this, LocalBackStack provides backStack) {
            NavDisplay(
                backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->
                    when (key) {
                        is Screen.Home -> NavEntry(key) {
                            HomeScreen(contentPadding)
                        }

                        is Screen.Projects -> NavEntry(key) {
                            ProjectsScreen(contentPadding)
                        }
                    }
                })
        }
    }
}
