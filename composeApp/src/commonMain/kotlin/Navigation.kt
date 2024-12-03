import androidx.compose.animation.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import locals.LocalNavController
import model.MainViewModel
import screens.HomeScreen
import screens.LoadingScreen
import screens.ProjectScreen
import screens.ProjectsScreen

@Composable
fun Navigation(contentPadding: PaddingValues, vm: MainViewModel = viewModel { MainViewModel() }) {
    NavHost(
        LocalNavController.current, "home",
        enterTransition = { fadeIn() + scaleIn(initialScale = 0.95f) },
        exitTransition = { fadeOut() + scaleOut(targetScale = 0.95f) }) {
        composable("home") {
            HomeScreen(contentPadding)
        }

        composable("projects") {
            val repoState by vm.repoState.collectAsState()

            LaunchedEffect(Unit) {
                vm.loadRepositories()
            }
            AnimatedContent(repoState) { current ->
                when (current) {
                    is MainViewModel.RepoState.Loaded -> ProjectsScreen(current.repos, contentPadding, this@composable)
                    is MainViewModel.RepoState.Loading -> LoadingScreen("Loading...")
                    else -> Text("oops")
                }
            }
        }

        composable(
            "projects/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {

            val id = it.arguments?.getInt("id")
            val repoState by vm.repoState.collectAsState()

            LaunchedEffect(Unit) {
                vm.loadRepositories()
            }

            AnimatedContent(repoState) { current ->
                when (current) {
                    is MainViewModel.RepoState.Loaded -> ProjectScreen(
                        current.repos.find { it.id == id }!!,
                        contentPadding,
                        this@composable
                    )

                    else -> Text("no worky")
                }
            }
        }
    }
}
