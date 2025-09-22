import androidx.compose.animation.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import locals.LocalBackStack
import locals.LocalSharedTransitionScope
import screens.*

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(contentPadding: PaddingValues) {
    val backStack = rememberSerializable(serializer = SnapshotStateListSerializer<Screen>()) {
        mutableStateListOf(Screen.Home)
    }

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this, LocalBackStack provides backStack) {
            NavDisplay(
                backStack,
                onBack = { backStack.removeLastOrNull() },
                transitionSpec = {
                    fadeIn() + scaleIn(initialScale = 0.9f) togetherWith fadeOut() + scaleOut(targetScale = 1.1f)
                },
                popTransitionSpec = {
                    fadeIn() + scaleIn(initialScale = 1.1f) togetherWith fadeOut() + scaleOut(targetScale = 0.9f)
                },
                entryProvider = { key ->
                    when (key) {
                        is Screen.Home -> NavEntry(key) {
                            HomeScreen(contentPadding)
                        }

                        is Screen.Projects -> NavEntry(key) {
                            ProjectsScreen(contentPadding)
                        }

                        is Screen.Projects.Single -> NavEntry(key) {
                            ProjectScreen(contentPadding)
                        }

                        is Screen.Media -> NavEntry(key) {
                            MediaScreen(contentPadding)
                        }

                        is Screen.About -> NavEntry(key) {
                            AboutScreen(contentPadding)
                        }
                    }
                })
        }
    }
}
