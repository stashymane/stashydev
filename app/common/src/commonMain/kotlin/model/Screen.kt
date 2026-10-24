package model

import androidx.navigation3.runtime.NavKey
import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {
    @Serializable
    sealed class Popup : Screen() {
        //TODO popup screens
    }

    @Serializable
    @Resource("/")
    data object Home : Screen()

    @Serializable
    @Resource("/projects")
    data object Projects : Screen()

    @Serializable
    @Resource("/media")
    data object Media : Screen()

    @Serializable
    @Resource("/about")
    data object About : Screen()
}

val TopLevelRoutes: Set<Screen> = setOf(
    Screen.Home,
    Screen.Projects,
    Screen.Media,
    Screen.About,
)
