package components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ForkRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import components.util.AnimatedLaunch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import locals.LocalNavController
import model.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavbar(modifier: Modifier = Modifier.width(300.dp)) {
    val navController = LocalNavController.current
    val uriHandler = LocalUriHandler.current

    val backState by navController.currentBackStackEntryAsState()
    val destination = backState?.destination

    val gradient = Brush.verticalGradient(
        listOf(
            Color.White.copy(alpha = 0.05f).compositeOver(MaterialTheme.colorScheme.background), Color.Transparent
        )
    )

    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(Modifier.height(46.dp).fillMaxWidth()) {
                AnimatedLaunch(
                    0f..1f,
                    {
                        alpha = it
                        translationY = (1 - it) * 10
                    },
                    spec = tween(1000, easing = LinearOutSlowInEasing)
                ) { modifier ->
                    Text("stashymane", modifier = Modifier.align(Alignment.Center).then(modifier))
                }
            }

            Row {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip(caretSize = DpSize(8.dp, 4.dp)) {
                            Text("GitHub")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    NavLink(
                        icon = { Icon(Icons.Default.ForkRight, null) },
                        isActive = false
                    ) { uriHandler.openUri("https://github.com/stashymane") }
                }

                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip(caretSize = DpSize(8.dp, 4.dp)) {
                            Text("Email")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    NavLink(
                        icon = { Icon(Icons.Default.Mail, null) },
                        isActive = false
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
                    icon = { Icon(Icons.AutoMirrored.Default.List, null) },
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

@Composable
fun NavLink(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    isActive: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val backgroundColor by animateColorAsState(if (isActive) LocalContentColor.current.copy(alpha = 0.2f) else Color.Transparent)

    LaunchedEffect(interactionSource) {
        interactionSource.interactions
            .filterIsInstance<HoverInteraction.Enter>()
            .onEach {
                //play sound
            }.collect()
    }

    Surface(
        onClick,
        modifier = modifier.width(IntrinsicSize.Max),
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            icon()
            title()
        }
    }
}
