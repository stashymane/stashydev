package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.projects.RepoCard
import httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import model.GithubRepo

@Composable
fun ProjectsScreen(contentPadding: PaddingValues) {
    val scrollState = rememberScrollState()
    var repositories: List<GithubRepo> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(Unit) {
        try {
            val result = httpClient.get("https://api.github.com/users/stashymane/repos")
            repositories = result.body<List<GithubRepo>>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
        Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Projects", style = MaterialTheme.typography.displayLarge)

            LazyVerticalGrid(
                columns = GridCells.Adaptive(280.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(repositories) {
                    RepoCard(it, Modifier.fillMaxSize().weight(1f))
                }
            }
        }
    }
}