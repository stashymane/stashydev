import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import dev.stashy.navigation.navGraph
import dev.stashy.standard.ui.responsive.ContainerSize.Large
import dev.stashy.standard.ui.responsive.ContainerSize.Medium
import model.Screen
import screens.about.AboutScreen
import screens.home.HomeScreen
import screens.media.MediaScreen
import screens.projects.ProjectsScreen
import ui.modifiers.outerBorder
import ui.nav.ResponsiveScene

@Composable
private fun Modifier.defaultSceneEffects() = background(MaterialTheme.colorScheme.surface)
    .outerBorder(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

fun appNavGraph() = navGraph<NavKey> {
    group {
        entry<Screen.Home>(
            metadata = ResponsiveScene.configure {
                size = Medium
                effects = Modifier::defaultSceneEffects
                showNavigation = false
                alignment = Center
            }
        ) { HomeScreen() }
    }
    group {
        entry<Screen.Projects>(
            metadata = ResponsiveScene.configure {
                size = Large
                effects = Modifier::defaultSceneEffects
                showNavigation = true
                fillMaxSize = true
            }
        ) { ProjectsScreen() }
    }
    group {
        entry<Screen.Media>(
            metadata = ResponsiveScene.configure {
                size = Large
                effects = Modifier::defaultSceneEffects
                showNavigation = true
                fillMaxSize = true
            }
        ) { MediaScreen() }
    }
    group {
        entry<Screen.About>(metadata = ResponsiveScene.configure {
            size = Large
            effects = Modifier::defaultSceneEffects
            showNavigation = true
            fillMaxSize = true
        }) { AboutScreen() }
    }
}
