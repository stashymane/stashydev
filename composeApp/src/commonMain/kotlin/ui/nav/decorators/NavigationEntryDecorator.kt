package ui.nav.decorators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import dev.chrisbanes.haze.blur.HazeProgressive
import dev.chrisbanes.haze.blur.blurEffect
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import model.Screen
import ui.LocalScaffoldPadding
import ui.LocalSettings
import ui.components.nav.NavBar

class NavigationEntryDecorator : NavEntryDecorator<Screen>(
    decorate = { entry ->
        val showNavigation = entry.metadata[MetadataKey] ?: false
        val bottomNav = LocalSettings.current.bottomNav

        if (showNavigation) {
            val hazeState = rememberHazeState()

            val navModifier = Modifier.height(80.dp).hazeEffect(hazeState) {
                blurEffect {
                    noiseFactor = 0f
                    progressive = HazeProgressive.verticalGradient(
                        startIntensity = if (bottomNav) 0f else 1f,
                        endIntensity = if (bottomNav) 1f else 0f
                    )
                }
            }

            Scaffold(
                topBar = { if (!bottomNav) NavBar(navModifier) },
                bottomBar = { if (bottomNav) NavBar(navModifier) }
            ) {
                Box(Modifier.hazeSource(hazeState)) {
                    CompositionLocalProvider(LocalScaffoldPadding provides it) {
                        entry.Content()
                    }
                }
            }
        } else entry.Content()
    }
) {
    object MetadataKey : NavMetadataKey<Boolean>
}
