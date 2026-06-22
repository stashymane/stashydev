package ui.components.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.filled.ChevronBackward
import model.NavEntry
import org.jetbrains.compose.resources.stringResource
import ui.LocalBackStack
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.inDp
import ui.theme.navigationSharedElement

@OptIn(ExperimentalGridApi::class)
@Composable
fun NavBar(
    modifier: Modifier = Modifier.height(80.dp)
) {
    val backStack = LocalBackStack.current

    Grid(
        {
            column(64.dp)

            column(1.fr)
            column(1.fr)
            column(1.fr)

            row(1.fr)

            gap(8.dp)
        },
        modifier.padding(8.dp)
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            val iconSize = LocalTextStyle.current.lineHeight.inDp()

            NavLink(
                title = {},
                icon = { Icon(Icons.Filled.ChevronBackward, "back", Modifier.size(iconSize)) },
                isActive = false,
                modifier = Modifier.fillMaxSize().aspectRatio(1f, true)
            ) {
                backStack.removeLast()
            }

            NavEntry.All.forEach { entry ->
                NavLink(
                    title = {
                        Text(stringResource(entry.title))
                        Spacer(Modifier.fillMaxWidth())
                    },
                    icon = {
                        Icon(entry.icon, null, Modifier.size(iconSize))
                    },
                    isActive = backStack.currentGroup == entry.screen.group,
                    modifier = Modifier.fillMaxSize()
                ) {
                    backStack.add(entry.screen)
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun NavBarPreview() = PreviewHost {
    NavBar()
}
