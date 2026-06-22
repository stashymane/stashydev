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
import ui.locals.LocalBackStack
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.inDp

@OptIn(ExperimentalGridApi::class)
@Composable
fun NavBar(
    modifier: Modifier = Modifier
) {
    val backStack = LocalBackStack.current

    Grid(
        {
            column(64.dp)

            column(1.fr)
            column(1.fr)
            column(1.fr)

            row(GridTrackSize.Auto)

            gap(16.dp)
        },
        Modifier.height(64.dp)
            .then(modifier)
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            val iconSize = LocalTextStyle.current.lineHeight.inDp()

            NavLink(
                title = {},
                icon = { Icon(Icons.Filled.ChevronBackward, "back", Modifier.size(iconSize)) },
                isActive = false,
                modifier = Modifier.fillMaxHeight().aspectRatio(1f, true)
            ) {
                backStack.removeLast()
            }

            NavEntry.All.forEach { entry ->
                NavLink(
                    title = {
                        Text(stringResource(entry.title))
                    },
                    icon = {
                        Icon(entry.icon, null, Modifier.size(iconSize))
                    },
                    isActive = backStack.currentGroup == entry.screen.group,
                    modifier = Modifier.fillMaxWidth()
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
