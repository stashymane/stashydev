import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.decode.SkiaImageDecoder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.serviceLoaderEnabled
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import components.SiteFooter
import components.SiteHeader
import components.nav.NavBlock
import dev.stashy.home.Res
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
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
            val background =
                ImageRequest.Builder(LocalPlatformContext.current).data(Res.getUri("drawable/background.jpg"))
                    .crossfade(true).build()

            SharedTransitionLayout {
                CompositionLocalProvider(LocalSharedTransitionScope provides this@SharedTransitionLayout) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            background,
                            null,
                            Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alpha = 0.1f
                        )

                        Column(
                            Modifier.widthIn(max = 900.dp).heightIn(max = 720.dp).padding(contentPadding),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SiteHeader()
                            Row(
                                Modifier.padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                NavBlock(
                                    Modifier.weight(1f),
                                    onClick = {},
                                    icon = Icons.OutlineLarge.Cases,
                                    text = "Projects",
                                    background = {
                                        AsyncImage(
                                            Res.getUri("drawable/spaceman.gif"),
                                            null,
                                            contentScale = ContentScale.Crop
                                        )
                                    })
                                NavBlock(
                                    Modifier.weight(1f),
                                    onClick = {},
                                    icon = Icons.OutlineLarge.FitScreen,
                                    text = "Media",
                                    background = {
                                        AsyncImage(
                                            Res.getUri("drawable/wobble.gif"),
                                            null,
                                            contentScale = ContentScale.Crop
                                        )
                                    })
                                NavBlock(
                                    Modifier.weight(1f),
                                    onClick = {},
                                    icon = Icons.OutlineLarge.UserSearch,
                                    text = "About",
                                    background = {
                                        AsyncImage(
                                            Res.getUri("drawable/skroll.gif"),
                                            null,
                                            contentScale = ContentScale.Crop
                                        )
                                    })
                            }
                            SiteFooter()
                        }
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
