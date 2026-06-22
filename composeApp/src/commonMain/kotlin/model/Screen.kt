package model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ui.nav.MultiBackStack
import ui.nav.decorators.NavigationEntryDecorator
import ui.nav.decorators.ResponsiveNavEntryDecorator
import ui.screens.AboutScreen
import ui.screens.HomeScreen
import ui.screens.MediaScreen
import ui.screens.ProjectsScreen
import ui.theme.ContainerSize

@Serializable
sealed class Screen(
    override val group: Group? = null,
    val name: String? = null,
) : MultiBackStack.Entry<Screen.Group> {
    @Transient
    open val size: ContainerSize? = null

    @Transient
    open val showNavigation: Boolean = false

    @Transient
    open val backgroundColor: (@Composable () -> Color)? = null

    fun provideEntry(): NavEntry<Screen> = NavEntry(
        this,
        metadata = metadata {
            put(NavigationEntryDecorator.MetadataKey, showNavigation)
            size?.let { put(ResponsiveNavEntryDecorator.MetadataKey, it.value) }
            group?.let { put(Group.MetaKey, it) }
        }
    ) {
        Content()
    }

    @Composable
    abstract fun Content()

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
        }

        object MetaKey : NavMetadataKey<Group>
    }

    @Serializable
    sealed class Popup : Screen() {
        //TODO popup screens
    }

    @Serializable
    data object Home : Screen(Group.Home) {
        override val size: ContainerSize = ContainerSize.Regular

        @Composable
        override fun Content() = HomeScreen()
    }

    @Serializable
    data object Projects : Screen(Group.Projects, "projects") {
        override val size: ContainerSize = ContainerSize.Wide
        override val showNavigation: Boolean = true
        override val backgroundColor: @Composable (() -> Color) = { MaterialTheme.colorScheme.surface }

        @Composable
        override fun Content() = ProjectsScreen()
    }

    @Serializable
    data object Media : Screen(Group.Media, "media") {
        override val size: ContainerSize = ContainerSize.Wide
        override val showNavigation: Boolean = true
        override val backgroundColor: @Composable (() -> Color) = { MaterialTheme.colorScheme.surface }

        @Composable
        override fun Content() = MediaScreen()
    }

    @Serializable
    data object About : Screen(Group.About, "about") {
        override val size: ContainerSize = ContainerSize.Wide
        override val showNavigation: Boolean = true
        override val backgroundColor: @Composable (() -> Color) = { MaterialTheme.colorScheme.surface }

        @Composable
        override fun Content() = AboutScreen()
    }
}
