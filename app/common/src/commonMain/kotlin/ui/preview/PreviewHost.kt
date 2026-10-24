package ui.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import dev.stashy.navigation.compose.LocalNavigator
import dev.stashy.navigation.compose.rememberNavigationState
import dev.stashy.navigation.compose.rememberNavigator
import model.Screen
import model.TopLevelRoutes
import ui.LocalSharedTransitionScope
import ui.theme.AppTheme

@Composable
fun PreviewHost(
    content: @Composable () -> Unit
) {
    val state = rememberNavigationState(
        startRoute = Screen.Home,
        topLevelRoutes = TopLevelRoutes,
    )
    val navigator = rememberNavigator<Screen>(state)

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
