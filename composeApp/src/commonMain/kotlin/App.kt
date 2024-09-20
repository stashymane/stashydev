import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import components.AppNavbar
import kotlinx.serialization.json.Json
import locals.LocalNavController
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.HomeScreen
import screens.ProjectsScreen
import stashydev.composeapp.generated.resources.Res
import stashydev.composeapp.generated.resources.background
import theme.AppTheme

val json = Json {
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val navController = rememberNavController()
//    val bg = Res.getUri("drawable/background.jpg")

//    val dots = imageResource(Res.drawable.texture)
//    val brush = ShaderBrush(ImageShader(dots))

    AppTheme(Color.Red, true) {
        CompositionLocalProvider(LocalNavController provides navController) {
            Scaffold(topBar = {
                AppNavbar()
            }) { contentPadding ->
                Box {
                    AsyncImage(
                        Res.drawable.background,
                        null,
                        Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
//                    Image(
//                        painterResource(bg),
//                        contentDescription = "Background",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.FillBounds
//                    )
                    NavHost(LocalNavController.current, "home",
                        enterTransition = {
                            fadeIn() + scaleIn(initialScale = 0.95f)
                        },
                        exitTransition = {
                            fadeOut() + scaleOut(targetScale = 0.95f)
                        }) {
                        composable("home") {
                            HomeScreen(contentPadding)
                        }
                        composable("projects") {
                            ProjectsScreen(contentPadding)
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