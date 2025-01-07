import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.decode.SkiaImageDecoder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.serviceLoaderEnabled
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import components.nav.AppNavbar
import kotlinx.serialization.json.Json
import locals.LocalSharedTransitionScope
import org.jetbrains.compose.ui.tooling.preview.Preview
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

    AppTheme(Color.Blue, true) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ) { contentPadding ->
            Row {
                AppNavbar()

                SharedTransitionLayout {
                    CompositionLocalProvider(LocalSharedTransitionScope provides this@SharedTransitionLayout) {
                        Navigation(contentPadding)
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
