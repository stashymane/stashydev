import androidx.compose.animation.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import locals.LocalNavController
import model.Destination
import model.MainViewModel
import screens.HomeScreen
import screens.LoadingScreen
import screens.ProjectScreen
import screens.ProjectsScreen

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

        navigation<Destination.Projects>(startDestination = Destination.Projects.List) {
            composable<Destination.Projects.List> {
                val repoState by vm.repoState.collectAsState()

                LaunchedEffect(Unit) {
                    vm.loadRepositories()
                }
                AnimatedContent(repoState) { current ->
                    when (current) {
                        is MainViewModel.RepoState.Loaded -> ProjectsScreen(
                            current.repos,
                            contentPadding,
                            this@composable
                        )

                        is MainViewModel.RepoState.Loading -> LoadingScreen("Loading...")
                        else -> Text("oops")
                    }
                }
            }

            composable<Destination.Projects.Id> {
                val id = it.toRoute<Destination.Projects.Id>().id
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
}
