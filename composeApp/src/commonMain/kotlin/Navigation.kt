import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import locals.LocalNavController
import model.Destination
import model.MainViewModel
import screens.HomeScreen

@Composable
fun Navigation(contentPadding: PaddingValues, vm: MainViewModel = viewModel { MainViewModel() }) {
    NavHost(
        LocalNavController.current,
        Destination.Home,
        enterTransition = { fadeIn() + scaleIn(initialScale = 0.95f) },
        exitTransition = { fadeOut() + scaleOut(targetScale = 0.95f) }) {
        composable<Destination.Home> {
            HomeScreen(contentPadding)
        }

        navigation<Destination.Projects>(Destination.Projects.List) {
            composable<Destination.Projects.List> {
                Text("project list")
            }
        }
    }
}
