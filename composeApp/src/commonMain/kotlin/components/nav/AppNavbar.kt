package components.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import icons.Briefcase
import icons.LogoGithub
import icons.LogoTwitter
import locals.LocalNavController
import model.Destination
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun AppNavbar(modifier: Modifier = Modifier.width(240.dp)) {
    val navController = LocalNavController.current
    val uriHandler = LocalUriHandler.current

    val backState by navController.currentBackStackEntryAsState()
    val destination = backState?.destination

    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(Modifier.height(46.dp).fillMaxWidth()) {
                Text("stashymane", modifier = Modifier.align(Alignment.Center))
            }

            Row {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                    tooltip = {
                        PlainTooltip(caretShape = TooltipDefaults.caretShape()) {
                            Text("GitHub")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    NavLink(
                        icon = { Icon(icons.Icons.LogoGithub, null, Modifier.size(24.dp)) },
                        isActive = false,
                        isExternal = true
                    ) { uriHandler.openUri("https://github.com/stashymane") }
                }

                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                    tooltip = {
                        PlainTooltip(caretShape = TooltipDefaults.caretShape()) {
                            Text("Twitter")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    NavLink(
                        icon = { Icon(icons.Icons.LogoTwitter, null, Modifier.size(24.dp)) },
                        isActive = false,
                        isExternal = true
                    ) { uriHandler.openUri("https://twitter.com/stashyymane") }
                }

                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                    tooltip = {
                        PlainTooltip(caretShape = TooltipDefaults.caretShape()) {
                            Text("Email")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    NavLink(
                        icon = { Icon(Icons.Default.Mail, null, Modifier.size(24.dp)) },
                        isActive = false,
                        isExternal = true
                    ) { uriHandler.openUri("mailto:me@stashy.dev") }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                NavLink(
                    modifier = Modifier.fillMaxWidth(),
                    title = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, null) },
                    isActive = destination?.hasRoute<Destination.Home>() == true
                ) { navController.navigate(Destination.Home) }

                NavLink(
                    modifier = Modifier.fillMaxWidth(),
                    title = { Text("Projects") },
                    icon = { Icon(icons.Icons.Briefcase, null, Modifier.size(24.dp)) },
                    isActive = destination?.hasRoute<Destination.Projects.List>() == true
                ) { navController.navigate(Destination.Projects.List) }
            }
        }

        Spacer(
            Modifier.fillMaxHeight()
                .width(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                .align(Alignment.TopEnd)
        )
    }
}
