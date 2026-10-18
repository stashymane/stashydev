package ui.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import appNavGraph
import dev.stashy.navigation.Navigator
import dev.stashy.navigation.compose.LocalNavigator
import dev.stashy.navigation.compose.rememberNavigationState
import model.Screen
import ui.LocalSharedTransitionScope
import ui.theme.AppTheme

@Composable
fun PreviewHost(
    content: @Composable () -> Unit
) {
    val graph = remember { appNavGraph() }
    val state = rememberNavigationState(startRoute = Screen.Home, graph = graph)
    val navigator = remember(state, graph) { Navigator(state, graph) }

    AppTheme(Color.Blue, isSystemInDarkTheme()) {
        AnimatedContent(Unit) {
            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalNavAnimatedContentScope provides this@AnimatedContent,
                    LocalNavigator provides navigator,
                ) {
                    Surface(content = content)
                }
            }
        }
    }
}
