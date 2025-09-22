package screens

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Projects : Screen() {
        @Serializable
        data class Single(val id: String) : Screen()
    }

    @Serializable
    data object Media : Screen()

    @Serializable
    data object About : Screen()
}
