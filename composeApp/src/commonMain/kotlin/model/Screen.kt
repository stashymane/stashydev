package model

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.metadata
import kotlinx.serialization.Serializable
import ui.nav.MultiBackStack
import ui.nav.decorators.ResponsiveNavEntryDecorator
import ui.screens.AboutScreen
import ui.screens.HomeScreen
import ui.screens.MediaScreen
import ui.screens.ProjectsScreen
import ui.theme.ContainerSize

@Serializable
sealed class Screen(
    override val group: Group? = null
) : MultiBackStack.Entry<Screen.Group> {
    open val size: ContainerSize? = null

    fun provideEntry(): NavEntry<Screen> = NavEntry(
        this,
        metadata = metadata {
            size?.let { size ->
                put(ResponsiveNavEntryDecorator.MetadataKey, size.value)
            }
        }
    ) {
        content()
    }

    @Composable
    abstract fun content()

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
    data object Home : Screen(Group.Home) {
        override val size: ContainerSize = ContainerSize.Regular

        @Composable
        override fun content() = HomeScreen()
    }

    @Serializable
    data object Projects : Screen(Group.Projects) {
        override val size: ContainerSize = ContainerSize.Wide

        @Composable
        override fun content() = ProjectsScreen()
    }

    @Serializable
    data object Media : Screen(Group.Media) {
        override val size: ContainerSize = ContainerSize.Wide

        @Composable
        override fun content() = MediaScreen()
    }

    @Serializable
    data object About : Screen(Group.About) {
        override val size: ContainerSize = ContainerSize.Wide

        @Composable
        override fun content() = AboutScreen()
    }
}
