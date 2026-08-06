import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.serviceLoaderEnabled
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import dev.stashy.navigation.MultiBackStack
import dev.stashy.navigation.SyncBrowserHistory
import model.Screen
import org.koin.compose.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import ui.LocalBackStack
import ui.LocalContainerSize
import ui.Navigation
import ui.PreloadContent
import ui.preview.DevicePreview
import ui.theme.AppTheme
import ui.theme.currentContainerSize

typealias AppBackStack = MultiBackStack<Screen, Screen.Group>

@OptIn(ExperimentalSharedTransitionApi::class, KoinExperimentalAPI::class)
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(KtorNetworkFetcherFactory())
                add(SvgDecoder.Factory())
            }
            .serviceLoaderEnabled(true)
            .logger(DebugLogger())
            .build()
    }

    KoinApplication(koinConfiguration {
        modules(KoinModule)
    }) {
        val backStack: AppBackStack = remember { AppBackStack(Screen.Home) }
        val containerSize = currentContainerSize()

        SyncBrowserHistory(
            backStack,
            pathOf = Screen::toPath,
            parsePath = { Screen.fromPath(it) ?: Screen.Home },
        )
        PreloadContent()

        AppTheme(Color(0xFFc27aff)) {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLowest) {
                CompositionLocalProvider(
                    LocalBackStack provides backStack,
                    LocalContainerSize provides containerSize
                ) {
                    Navigation()
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun AppPreview() {
    App()
}
