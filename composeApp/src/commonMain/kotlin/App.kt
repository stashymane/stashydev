import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import kotlinx.serialization.json.Json
import model.AppState
import model.Screen
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import ui.LoadingState
import ui.LocalBackStack
import ui.LocalContainerSize
import ui.Navigation
import ui.loadContent
import ui.preview.DevicePreview
import ui.screens.LoadingScreen
import ui.theme.AppTheme
import ui.theme.currentContainerSize

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
                add(SvgDecoder.Factory())
            }
            .serviceLoaderEnabled(true)
            .logger(DebugLogger())
            .build()
    }

    KoinApplication(koinConfiguration {
        modules(KoinModule)
    }) {
        val state: AppState = koinInject()
        val backStack: AppBackStack = remember { AppBackStack(Screen.Home) }
        val containerSize = currentContainerSize()

        val loadingState by loadContent()
        val isComplete by remember { derivedStateOf { loadingState is LoadingState.Complete } }
        val progress = when (loadingState) {
            is LoadingState.Loading -> (loadingState as LoadingState.Loading).progress
            is LoadingState.Complete -> 1f
        }

        LaunchedEffect(Unit) {
            state.loadProjects()
        }

        AppTheme(Color(0xFFc27aff)) {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLowest) {
                CompositionLocalProvider(
                    LocalBackStack provides backStack,
                    LocalContainerSize provides containerSize
                ) {
                    SyncBrowserHistory(
                        backStack,
                        pathOf = Screen::toPath,
                        parsePath = { Screen.fromPath(it) ?: Screen.Home },
                    )

                    AnimatedContent(
                        isComplete,
                        transitionSpec = { fadeIn() togetherWith fadeOut() }
                    ) {
                        when (it) {
                            true -> Navigation()
                            false -> LoadingScreen(progress = progress) {
                                Text("hold on...")
                            }
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
