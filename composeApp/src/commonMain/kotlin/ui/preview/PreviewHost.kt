package ui.preview

import AppBackStack
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import model.Screen
import ui.locals.LocalBackStack
import ui.locals.LocalSharedTransitionScope
import ui.theme.AppTheme

@Composable
fun PreviewHost(
    content: @Composable () -> Unit
) {
    val backStack: AppBackStack = remember { AppBackStack(Screen.Home) }

    AppTheme(Color.Blue, isSystemInDarkTheme()) {
        AnimatedContent(Unit) {
            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalNavAnimatedContentScope provides this@AnimatedContent,
                    LocalBackStack provides backStack
                ) {
                    Surface {
                        content()
                    }
                }
            }
        }
    }
}
