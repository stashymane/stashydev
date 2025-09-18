import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.LoadingScreen
import theme.AppTheme

val json = Json {
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalSharedTransitionApi::class)
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

    val loadingState by loadContent()
    val isComplete by remember { derivedStateOf { loadingState is LoadingState.Complete } }

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
                AnimatedContent(isComplete) {
                    when (it) {
                        true -> Navigation(contentPadding)
                        false -> LoadingScreen(progress = (loadingState as? LoadingState.Loading)?.progress) { }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun AppPreview() {
    App()
}
