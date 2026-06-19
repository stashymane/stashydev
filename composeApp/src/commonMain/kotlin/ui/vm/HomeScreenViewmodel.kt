package ui.vm

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dev.stashy.home.Res
import dev.stashy.home.block_about_1k
import dev.stashy.home.block_media_1k
import dev.stashy.home.block_projects_1k
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import model.Screen
import org.jetbrains.compose.resources.DrawableResource

class HomeScreenViewmodel : ViewModel() {
    val cards: List<HomeScreenCard> = listOf(
        HomeScreenCard(
            "projects",
            "Projects",
            Screen.Projects,
            Icons.OutlineLarge.Cases,
            Res.drawable.block_projects_1k
        ),
        HomeScreenCard(
            "media",
            "Media",
            Screen.Media,
            Icons.OutlineLarge.FitScreen,
            Res.drawable.block_media_1k
        ),
        HomeScreenCard(
            "about",
            "About",
            Screen.About,
            Icons.OutlineLarge.UserSearch,
            Res.drawable.block_about_1k
        )
    )
}

data class HomeScreenCard(
    val id: String,
    val title: String,
    val screen: Screen,
    val icon: ImageVector,
    val background: DrawableResource
)
