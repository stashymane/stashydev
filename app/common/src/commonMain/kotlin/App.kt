import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import coil3.ImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.serviceLoaderEnabled
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import dev.stashy.home.Res
import dev.stashy.navigation.MultiBackStack
import dev.stashy.navigation.SyncBrowserHistory
import model.Screen
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import tiled.toTiledBrush
import ui.LocalBackStack
import ui.LocalContainerSize
import ui.Navigation
import ui.PreloadContent
import ui.modifiers.dotGridOverlay
import ui.preview.DevicePreview
import ui.theme.AppTheme
import ui.theme.currentContainerSize

typealias AppBackStack = MultiBackStack<Screen, Screen.Group>

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

        AppTheme(Color(0xFF9476FF)) {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLowest) {
                CompositionLocalProvider(
                    LocalBackStack provides backStack,
                    LocalContainerSize provides containerSize
                ) {
                    Box {
                        BackgroundImageOverlay(
                            "drawable/brick_wall_006_diff_2k.webp",
                            Modifier.matchParentSize(),
                            0.075f
                        )
                        Box(Modifier.matchParentSize().dotGridOverlay())
                        Navigation()
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundImageOverlay(
    uri: String,
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    val context = LocalPlatformContext.current
    val backgroundPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(Res.getUri(uri))
            .crossfade(false)
            .build()
    )
    val backgroundState by backgroundPainter.state.collectAsState()
    val backgroundBrush = remember(backgroundState) {
        val success = backgroundState as? AsyncImagePainter.State.Success
            ?: return@remember SolidColor(Color.Transparent)
        success.result.image.toTiledBrush()
    }

    AnimatedContent(
        backgroundBrush,
        modifier,
        { fadeIn() togetherWith fadeOut() }
    ) { backgroundBrush ->
        Box(Modifier.fillMaxSize().background(backgroundBrush, alpha = alpha))
    }
}

@DevicePreview
@Composable
private fun AppPreview() {
    App()
}
