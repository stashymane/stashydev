import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import dev.stashy.standard.ui.responsive.ContainerSize.Large
import dev.stashy.standard.ui.responsive.ContainerSize.Medium
import model.Screen
import model.TopLevelRoutes
import screens.about.AboutScreen
import screens.home.HomeScreen
import screens.media.MediaScreen
import screens.projects.ProjectsScreen
import ui.modifiers.outerBorder
import ui.nav.LayoutConfig
import ui.nav.ResponsiveScene
import ui.nav.crossRootMetadata

@Composable
private fun Modifier.defaultSceneEffects() = background(MaterialTheme.colorScheme.surface)
    .outerBorder(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

private fun sceneMetadata(
    screen: Screen,
    config: LayoutConfig.() -> Unit,
): Map<String, Any> {
    val rootIndex = TopLevelRoutes.indexOf(screen)
    return ResponsiveScene.configure(config) +
            if (rootIndex >= 0) crossRootMetadata(rootIndex) else emptyMap()
}

fun appEntryProvider() = entryProvider<NavKey> {
    entry<Screen.Home>(
        metadata = sceneMetadata(Screen.Home) {
            size = Medium
            effects = Modifier::defaultSceneEffects
            showNavigation = false
            alignment = Center
        }
    ) { HomeScreen() }

    entry<Screen.Projects>(
        metadata = sceneMetadata(Screen.Projects) {
            size = Large
            effects = Modifier::defaultSceneEffects
            showNavigation = true
            fillMaxSize = true
        }
    ) { ProjectsScreen() }

    entry<Screen.Media>(
        metadata = sceneMetadata(Screen.Media) {
            size = Large
            effects = Modifier::defaultSceneEffects
            showNavigation = true
            fillMaxSize = true
        }
    ) { MediaScreen() }

    entry<Screen.About>(
        metadata = sceneMetadata(Screen.About) {
            size = Large
            effects = Modifier::defaultSceneEffects
            showNavigation = true
            fillMaxSize = true
        }
    ) { AboutScreen() }
}
