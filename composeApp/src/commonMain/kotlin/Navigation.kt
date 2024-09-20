import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import locals.LocalNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.HomeScreen
import screens.ProjectsScreen

@Composable
fun Navigation(contentPadding: PaddingValues) {
    NavHost(
        LocalNavController.current, "home",
        enterTransition = { fadeIn() + scaleIn(initialScale = 0.95f) },
        exitTransition = { fadeOut() + scaleOut(targetScale = 0.95f) }) {
        composable("home") {
            HomeScreen(contentPadding)
        }
        composable("projects") {
            ProjectsScreen(contentPadding)
        }
    }
}

@Preview
@Composable
private fun NavigationPreview() {

}