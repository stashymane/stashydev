package components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import locals.LocalNavController

@Composable
fun AppNavbar(modifier: Modifier = Modifier.fillMaxWidth()) {
    val navController = LocalNavController.current

    Box {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text("stashy.dev")
            Spacer(Modifier)
            NavLink(title = "Home", name = "home") { navController.navigate("home") }
            NavLink(title = "Projects", name = "projects") { navController.navigate("projects") }
            Spacer(Modifier.weight(1f))
            NavIcon(icon = Icons.Default.Videocam) {}
        }
    }
}

@Composable
fun NavLink(modifier: Modifier = Modifier, title: String, name: String, onClick: () -> Unit) {
    val backState by LocalNavController.current.currentBackStackEntryAsState()
    val isActive = backState?.destination?.route == name
    val backgroundColor by animateColorAsState(if (isActive) LocalContentColor.current.copy(alpha = 0.2f) else Color.Transparent)

    TextButton(onClick, colors = ButtonDefaults.textButtonColors(containerColor = backgroundColor)) {
        Text(title)
    }
}

@Composable
fun NavIcon(modifier: Modifier = Modifier, icon: ImageVector, onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(icon, contentDescription = null)
    }
}