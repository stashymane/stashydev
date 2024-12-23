import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import components.AppNavbar
import dev.stashy.home.Res
import dev.stashy.home.texture
import kotlinx.serialization.json.Json
import locals.LocalNavController
import locals.LocalSharedTransitionScope
import model.MainViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import theme.AppTheme

val json = Json {
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalResourceApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun App(vm: MainViewModel = koinViewModel()) {
    val navController = rememberNavController()

    val dotsBrush = ShaderBrush(ImageShader(imageResource(Res.drawable.texture), TileMode.Repeated, TileMode.Repeated))
    val background = ImageRequest.Builder(LocalPlatformContext.current)
        .data(Res.getUri("drawable/background.jpg"))
        .crossfade(true)
        .build()

    AppTheme(Color.Red, true) {
        CompositionLocalProvider(LocalNavController provides navController) {
            Scaffold(topBar = {
                AppNavbar()
            }) { contentPadding ->
                Box {
                    AsyncImage(
                        background,
                        null,
                        Modifier.matchParentSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    Box(Modifier.matchParentSize().background(dotsBrush))

                    SharedTransitionLayout {
                        CompositionLocalProvider(LocalSharedTransitionScope provides this@SharedTransitionLayout) {
                            Navigation(contentPadding)
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
