package model

import kotlinx.serialization.Serializable
import ui.nav.MultiBackStack

@Serializable
sealed class Screen(
    override val group: Group? = null
) : MultiBackStack.Entry<Screen.Group> {

    @Serializable
    data class Group(
        val name: String,
        val order: Int? = null
    ) {
        fun towards(target: Group?): Int {
            val sourceOrder = order ?: return 0
            val targetOrder = target?.order ?: return 0

            return sourceOrder.compareTo(targetOrder)
        }

        companion object {
            val Home = Group("home")
            val Projects = Group("projects", 0)
            val Media = Group("media", 1)
            val About = Group("about", 2)

            const val META_KEY: String = "screen.group"
        }
    }

    @Serializable
    sealed class Popup : Screen() {
        //TODO popup screens
    }

    @Serializable
    data object Home : Screen(Group.Home)

    @Serializable
    data object Projects : Screen(Group.Projects)

    @Serializable
    data object Media : Screen(Group.Media)

    @Serializable
    data object About : Screen(Group.About)
}
