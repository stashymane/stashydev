package model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.stashy.home.Res
import dev.stashy.home.nav_about
import dev.stashy.home.nav_media
import dev.stashy.home.nav_projects
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import org.jetbrains.compose.resources.StringResource

data class NavEntry(
    val screen: Screen,
    val title: StringResource,
    val icon: ImageVector
) {
    companion object {
        val All = listOf(
            NavEntry(
                Screen.Projects,
                Res.string.nav_projects,
                Icons.OutlineLarge.Cases
            ),
            NavEntry(
                Screen.Media,
                Res.string.nav_media,
                Icons.OutlineLarge.FitScreen
            ),
            NavEntry(
                Screen.About,
                Res.string.nav_about,
                Icons.OutlineLarge.UserSearch
            )
        )
    }
}
