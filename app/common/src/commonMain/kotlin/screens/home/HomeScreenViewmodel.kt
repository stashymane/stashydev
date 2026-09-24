package screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import compose.glorp
import compose.meshGradient
import compose.pixelGrid
import dev.stashy.home.Res
import dev.stashy.home.nav_about
import dev.stashy.home.nav_media
import dev.stashy.home.nav_projects
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import model.Screen
import org.jetbrains.compose.resources.StringResource

class HomeScreenViewmodel : ViewModel() {
    val cards: List<HomeScreenCard> = listOf(
        HomeScreenCard(
            Res.string.nav_projects,
            Screen.Projects,
            Icons.OutlineLarge.Cases,
            { Box(Modifier.matchParentSize().pixelGrid()) }
        ),
        HomeScreenCard(
            Res.string.nav_media,
            Screen.Media,
            Icons.OutlineLarge.FitScreen,
            { Box(Modifier.matchParentSize().meshGradient()) }
        ),
        HomeScreenCard(
            Res.string.nav_about,
            Screen.About,
            Icons.OutlineLarge.UserSearch,
            { Box(Modifier.matchParentSize().glorp()) }
        )
    )
}

data class HomeScreenCard(
    val title: StringResource,
    val screen: Screen,
    val icon: ImageVector,
    val background: @Composable BoxScope.() -> Unit
)
