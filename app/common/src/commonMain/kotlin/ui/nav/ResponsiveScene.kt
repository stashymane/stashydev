package ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ui.LocalScaffoldPadding
import ui.components.nav.NavBar
import ui.components.nav.navHazeEffect
import ui.nav.ResponsiveScene.MetadataKey
import ui.theme.*

data class LayoutConfig(
    var size: ContainerSize? = null,
    var effects: @Composable Modifier.() -> Modifier = { this },
    var showNavigation: Boolean = false,
    var alignment: Alignment = Alignment.TopCenter,
    var fillMaxSize: Boolean = false,
) {
    val screenWidth: Dp get() = size?.value ?: Dp.Unspecified
}

data class ResponsiveScene<T : Any>(
    override val key: Any,
    val entry: NavEntry<T>,
    override val entries: List<NavEntry<T>>,
    override val previousEntries: List<NavEntry<T>>,
    val config: LayoutConfig
) : Scene<T> {
    override val content: @Composable (() -> Unit) = {
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .animateBlur(
                    LocalNavAnimatedContentScope.current,
                    blurIn(instantBezier(), initialRadius = 16.dp),
                    blurOut(instantBezier(), targetRadius = 16.dp)
                ),
            contentAlignment = config.alignment
        ) {
            val hazeState = rememberHazeState()
            val applyEffects = config.effects

            Box(
                Modifier
                    .widthIn(max = config.screenWidth)
                    .then(
                        if (config.fillMaxSize) Modifier.fillMaxSize()
                        else Modifier.heightIn(max = maxHeight)
                    )
                    .applyEffects()
            ) {
                val navHeight = if (config.showNavigation) 80.dp else 0.dp

                Box(Modifier.hazeSource(hazeState)) {
                    CompositionLocalProvider(
                        LocalScaffoldPadding provides PaddingValues(top = navHeight)
                    ) {
                        entry.Content()
                    }
                }

                if (config.showNavigation) {
                    NavBar(
                        Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .height(navHeight)
                            .navHazeEffect(hazeState, MaterialTheme.colorScheme.surface)
                            .navigationSharedElement()
                    )
                }
            }
        }
    }

    object MetadataKey : NavMetadataKey<LayoutConfig>
    companion object {
        fun configure(config: LayoutConfig.() -> Unit): Map<String, Any> = metadata {
            put(MetadataKey, LayoutConfig().apply(config))
        }
    }
}

class ResponsiveSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val last = entries.last()
        val config: LayoutConfig = last.metadata[MetadataKey] ?: return null
        return ResponsiveScene(last.contentKey, last, entries, entries.dropLast(1), config)
    }
}
