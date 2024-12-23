import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import components.AppNavbar
import dev.stashy.home.Res
import kotlinx.serialization.json.Json
import locals.LocalSharedTransitionScope
import model.MainViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import theme.AppTheme

val json = Json {
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalResourceApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun App(vm: MainViewModel = koinViewModel()) {
    val background = ImageRequest.Builder(LocalPlatformContext.current)
        .data(Res.getUri("drawable/background.jpg"))
        .crossfade(true)
        .build()

    AppTheme(Color.Red, true) {
        Scaffold(topBar = {
            AppNavbar()
        }) { contentPadding ->
            Box {
                AsyncImage(
                    background,
                    null,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

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
