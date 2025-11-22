import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.decode.SkiaImageDecoder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.serviceLoaderEnabled
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import kotlinx.serialization.json.Json
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import preview.DevicePreview
import screens.LoadingScreen
import theme.AppTheme

val json = Json {
    ignoreUnknownKeys = true
}

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

        AppTheme(Color.Blue, true) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ) { contentPadding ->
                CompositionLocalProvider(
                    LocalScrollbarStyle provides ScrollbarStyle(
                        minimalHeight = 32.dp,
                        thickness = 8.dp,
                        shape = RectangleShape,
                        hoverDurationMillis = 300,
                        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f)
                    )
                ) {
                    AnimatedContent(
                        isComplete,
                        transitionSpec = {
                            fadeIn() togetherWith scaleOut(targetScale = 0.9f) + fadeOut()
                        }) {
                        when (it) {
                            true -> Navigation(contentPadding)
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
