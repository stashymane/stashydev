package model

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import dev.stashy.navigation.MultiBackStack
import dev.stashy.navigation.fromResourcePath
import dev.stashy.navigation.toResourcePath
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import screens.about.AboutScreen
import screens.home.HomeScreen
import screens.media.MediaScreen
import screens.projects.ProjectsScreen
import ui.modifiers.outerBorder
import ui.nav.ResponsiveScene

@Composable
private fun Modifier.defaultSceneEffects() = background(MaterialTheme.colorScheme.surface)
    .outerBorder(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

@Serializable
sealed class Screen(
    @Transient
    override val group: Group? = null,
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
    @Resource("/")
    data object Home : Screen(Group.Home) {
        @Composable
        override fun Content() = HomeScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Regular
            effects = { defaultSceneEffects() }
            showNavigation = false
            alignment = Alignment.Center
        }
    }

    @Serializable
    @Resource("/projects")
    data object Projects : Screen(Group.Projects) {
        @Composable
        override fun Content() = ProjectsScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Wide
            effects = { defaultSceneEffects() }
            showNavigation = true
            fillMaxSize = true
        }
    }

    @Serializable
    @Resource("/media")
    data object Media : Screen(Group.Media) {
        @Composable
        override fun Content() = MediaScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Wide
            effects = { defaultSceneEffects() }
            showNavigation = true
            fillMaxSize = true
        }
    }

    @Serializable
    @Resource("/about")
    data object About : Screen(Group.About) {
        @Composable
        override fun Content() = AboutScreen()

        override fun metadata(): Map<String, Any> = ResponsiveScene.configure {
            size = Wide
            effects = { defaultSceneEffects() }
            showNavigation = true
            fillMaxSize = true
        }
    }

    fun toPath(): String = toResourcePath()

    companion object {
        fun fromPath(path: String): Screen? = fromResourcePath(path)
    }
}
