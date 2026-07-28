package model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import kotlinx.serialization.Serializable
import ui.nav.MultiBackStack
import ui.nav.scenes.ResponsiveScene
import ui.screens.AboutScreen
import ui.screens.HomeScreen
import ui.screens.MediaScreen
import ui.screens.ProjectsScreen

@Serializable
sealed class Screen(
    override val group: Group? = null,
    val name: String? = null,
) : MultiBackStack.Entry<Screen.Group> {
    open fun metadata(): Map<String, Any> = metadata { }

    fun provideEntry(): NavEntry<Screen> = NavEntry(
        this,
        metadata = metadata() + metadata {
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
        @Composable
        override fun Content() = HomeScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Regular
            showNavigation = false
        }
    }

    @Serializable
    data object Projects : Screen(Group.Projects, "projects") {
        @Composable
        override fun Content() = ProjectsScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Wide
            backgroundColor = { MaterialTheme.colorScheme.surface }
            showNavigation = true
        }
    }

    @Serializable
    data object Media : Screen(Group.Media, "media") {
        @Composable
        override fun Content() = MediaScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Wide
            backgroundColor = { MaterialTheme.colorScheme.surface }
            showNavigation = true
        }
    }

    @Serializable
    data object About : Screen(Group.About, "about") {
        @Composable
        override fun Content() = AboutScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Wide
            backgroundColor = { MaterialTheme.colorScheme.surface }
            showNavigation = true
        }
    }
}
