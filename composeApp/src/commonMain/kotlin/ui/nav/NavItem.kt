package ui.nav

import AppBackStack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import icons.Icons
import icons.outline.Briefcase
import model.Screen
import ui.locals.LocalBackStack

data class NavItem(
    val root: Screen,
    val isCurrent: @Composable () -> Boolean,
    val name: @Composable () -> String,
    val icon: @Composable () -> ImageVector,
) {
    companion object {
        val Main = listOf<NavItem>(
            NavItem(
                Screen.Projects,
                { LocalBackStack.current.currently(Screen.Projects) },
                { "Projects" },
                { Icons.Outline.Briefcase }
            )
        )
    }
}

fun AppBackStack.currently(entry: MultiBackStack.Entry<Screen.Group>): Boolean =
    currentGroup == entry.group
