package ui.nav.scenes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import dev.chrisbanes.haze.blur.HazeProgressive
import dev.chrisbanes.haze.blur.blurEffect
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ui.LocalScaffoldPadding
import ui.components.nav.NavBar
import ui.nav.scenes.ResponsiveScene.MetadataKey
import ui.theme.ContainerSize

data class LayoutConfig(
    var size: ContainerSize? = null,
    var backgroundColor: (@Composable (() -> Color)) = { Color.Unspecified },
    var showNavigation: Boolean = false
) {
    val screenWidth: Dp get() = size?.value ?: Dp.Unspecified
}

class ResponsiveScene<T : Any>(
    override val key: Any,
    val entry: NavEntry<T>,
    override val entries: List<NavEntry<T>>,
    override val previousEntries: List<NavEntry<T>>,
    val config: LayoutConfig
) : Scene<T> {
    override val content: @Composable (() -> Unit) = {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val hazeState = rememberHazeState()

            Scaffold(
                Modifier.widthIn(max = config.screenWidth),
                containerColor = config.backgroundColor(),
                topBar = {
                    if (config.showNavigation) {
                        NavBar(Modifier.height(80.dp).hazeEffect(hazeState) {
                            blurEffect {
                                noiseFactor = 0f
                                progressive = HazeProgressive.verticalGradient(
                                    startIntensity = 1f,
                                    endIntensity = 0f
                                )
                            }
                        })
                    }
                }
            ) {
                Box(Modifier.hazeSource(hazeState)) {
                    CompositionLocalProvider(LocalScaffoldPadding provides it) {
                        entry.Content()
                    }
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
