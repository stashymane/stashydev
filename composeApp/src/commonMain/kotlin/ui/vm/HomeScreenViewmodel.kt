package ui.vm

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dev.stashy.home.*
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import model.Screen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class HomeScreenViewmodel : ViewModel() {
    val cards: List<HomeScreenCard> = listOf(
        HomeScreenCard(
            "projects",
            Res.string.nav_projects,
            Screen.Projects,
            Icons.OutlineLarge.Cases,
            Res.drawable.block_projects_1k
        ),
        HomeScreenCard(
            "media",
            Res.string.nav_media,
            Screen.Media,
            Icons.OutlineLarge.FitScreen,
            Res.drawable.block_media_1k
        ),
        HomeScreenCard(
            "about",
            Res.string.nav_about,
            Screen.About,
            Icons.OutlineLarge.UserSearch,
            Res.drawable.block_about_1k
        )
    )
}

data class HomeScreenCard(
    val id: String,
    val title: StringResource,
    val screen: Screen,
    val icon: ImageVector,
    val background: DrawableResource
)
