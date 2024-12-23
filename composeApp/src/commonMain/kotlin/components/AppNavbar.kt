package components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import locals.LocalNavController
import model.Destination

@Composable
fun AppNavbar(modifier: Modifier = Modifier.fillMaxWidth()) {
    val navController = LocalNavController.current
    val gradient = Brush.verticalGradient(
        listOf(
            Color.White.copy(alpha = 0.05f).compositeOver(MaterialTheme.colorScheme.background), Color.Transparent
        )
    )

    Box(modifier = Modifier.background(gradient)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text("stashy.dev")
            Spacer(Modifier)
            NavLink(title = "Home", isActive = { it?.hasRoute<Destination.Home>() == true }) {
                navController.navigate(Destination.Home)
            }
            NavLink(title = "Projects", isActive = { it?.hasRoute<Destination.Projects.List>() == true }) {
                navController.navigate(Destination.Projects.List)
            }
            Spacer(Modifier.weight(1f))
            NavIcon(icon = Icons.Default.Videocam) {}
        }
    }
}

@Composable
fun NavLink(modifier: Modifier = Modifier, title: String, isActive: (NavDestination?) -> Boolean, onClick: () -> Unit) {
    val interactionSource = MutableInteractionSource()
    val backState by LocalNavController.current.currentBackStackEntryAsState()
    val backgroundColor by animateColorAsState(
        if (isActive(backState?.destination))
            LocalContentColor.current.copy(alpha = 0.2f)
        else
            Color.Transparent
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions
            .filterIsInstance<HoverInteraction.Enter>()
            .onEach {
                //play sound
            }.collect()
    }

    TextButton(
        onClick,
        colors = ButtonDefaults.textButtonColors(containerColor = backgroundColor),
        modifier = Modifier.hoverable(interactionSource)
    ) {
        Text(title)
    }
}

@Composable
fun NavIcon(modifier: Modifier = Modifier, icon: ImageVector, onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(icon, contentDescription = null)
    }
}
