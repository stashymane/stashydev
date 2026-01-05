import androidx.compose.animation.*
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.decode.SkiaImageDecoder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.serviceLoaderEnabled
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import kotlinx.serialization.json.Json
import model.Screen
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import ui.LoadingState
import ui.Navigation
import ui.loadContent
import ui.locals.LocalBackStack
import ui.nav.MultiBackStack
import ui.preview.DevicePreview
import ui.screens.LoadingScreen
import ui.theme.AppTheme
import ui.theme.scrollbarStyle

val json = Json {
    ignoreUnknownKeys = true
}

typealias AppBackStack = MultiBackStack<Screen, Screen.Group>

@OptIn(ExperimentalSharedTransitionApi::class, KoinExperimentalAPI::class)
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(KtorNetworkFetcherFactory())
                add(SkiaImageDecoder.Factory())
                add(SvgDecoder.Factory())
            }
            .serviceLoaderEnabled(true)
            .logger(DebugLogger())
            .build()
    }

    KoinMultiplatformApplication(koinConfiguration {
        modules(KoinModule)
    }) {
        val loadingState by loadContent()
        val isComplete by remember { derivedStateOf { loadingState is LoadingState.Complete } }
        val progress = when (loadingState) {
            is LoadingState.Loading -> (loadingState as LoadingState.Loading).progress
            is LoadingState.Complete -> 1f
        }
        val backStack: AppBackStack = remember { AppBackStack(Screen.Home) }

        AppTheme(Color.Blue, true) {
            Surface {
                CompositionLocalProvider(
                    LocalScrollbarStyle provides scrollbarStyle(),
                    LocalBackStack provides backStack
                ) {
                    AnimatedContent(
                        isComplete,
                        transitionSpec = { fadeIn() togetherWith fadeOut() }) {
                        when (it) {
                            true -> Navigation()
                            false -> LoadingScreen(progress = progress) { }
                        }
                    }
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
