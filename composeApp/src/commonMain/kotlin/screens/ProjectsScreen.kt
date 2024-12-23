package screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.projects.RepoListItem
import locals.LocalNavController
import locals.LocalSharedTransitionScope
import model.Destination
import model.GithubRepo

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProjectsScreen(
    repositories: List<GithubRepo>,
    contentPadding: PaddingValues,
    animatedContentScope: AnimatedContentScope
) {
    val scrollState = rememberScrollState()
    val transitionScope = LocalSharedTransitionScope.current
    val navController = LocalNavController.current

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(480.dp),
        contentPadding = contentPadding
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(Modifier.padding(vertical = 16.dp, horizontal = 32.dp)) {
                Text("Projects", style = MaterialTheme.typography.displayLarge)
            }
        }

        items(repositories) { project ->
            with(transitionScope) {
                RepoListItem(
                    project,
                    Modifier.fillMaxSize().padding(horizontal = 16.dp).sharedElement(
                        transitionScope.rememberSharedContentState("project-${project.id}"),
                        animatedContentScope
                    ),
                ) {
                    navController.navigate(Destination.Projects.Id(project.id))
                }
            }
        }
    }
}
