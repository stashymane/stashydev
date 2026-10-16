import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
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
import dev.stashy.navigation.LocalNavigator
import dev.stashy.navigation.Navigator
import dev.stashy.navigation.compose.SyncBrowserHistory
import dev.stashy.navigation.compose.rememberNavigationState
import dev.stashy.standard.ui.responsive.LocalResponsiveInfo
import dev.stashy.standard.ui.responsive.rememberResponsiveInfo
import model.Screen
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import tiled.toTiledBrush
import ui.Navigation
import ui.PreloadContent
import ui.modifiers.dotGridOverlay
import ui.preview.DevicePreview
import ui.theme.AppTheme

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
        val state = rememberNavigationState(startRoute = Screen.Home)
        val navigator = remember { Navigator(state) }
        val responsiveInfo = rememberResponsiveInfo()

        SyncBrowserHistory<Screen>(navigator)
        PreloadContent()

        AppTheme(Color(0xFF9476FF)) {
            Surface(color = MaterialTheme.colorScheme.background) {
                CompositionLocalProvider(
                    LocalNavigator provides navigator,
                    LocalResponsiveInfo provides responsiveInfo
                ) {
                    Box {
                        BackgroundImageOverlay(
                            "drawable/brick_wall_006_diff_2k.webp",
                            Modifier.matchParentSize().graphicsLayer {
                                blendMode = BlendMode.Multiply
                            },
                            1f
                        )
                        Box(Modifier.matchParentSize().dotGridOverlay(MaterialTheme.colorScheme.background))
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
        { fadeIn(tween(1000)) togetherWith fadeOut(tween(1000)) }
    ) { backgroundBrush ->
        Box(Modifier.fillMaxSize().background(backgroundBrush, alpha = alpha))
    }
}

@DevicePreview
@Composable
private fun AppPreview() {
    App()
}
