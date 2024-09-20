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
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import components.AppNavbar
import kotlinx.serialization.json.Json
import locals.LocalNavController
import locals.LocalSharedTransitionScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import stashydev.composeapp.generated.resources.Res
import theme.AppTheme

val json = Json {
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalResourceApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun App() {
    val navController = rememberNavController()

//    val dots = imageResource(Res.drawable.texture)
//    val brush = ShaderBrush(ImageShader(dots))

    AppTheme(Color.Red, true) {
        CompositionLocalProvider(LocalNavController provides navController) {
            Scaffold(topBar = {
                AppNavbar()
            }) { contentPadding ->
                Box {
                    AsyncImage(
                        Res.getUri("drawable/background.jpg"),
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
}

@Composable
@Preview
private fun AppPreview() {
    App()
}