package screens

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    abstract val stack: Screen

    @Serializable
    data object Home : Screen() {
        override val stack = this
    }

    @Serializable
    data object Projects : Screen() {
        override val stack = this

        @Serializable
        data class Single(val id: String) : Screen() {
            override val stack = Projects
        }
    }

    @Serializable
    data object Media : Screen() {
        override val stack = this
    }

    @Serializable
    data object About : Screen() {
        override val stack = this
    }
}
