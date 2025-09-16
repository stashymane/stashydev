package screens

import Project
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import vm.HomeScreenViewmodel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(contentPadding: PaddingValues, vm: HomeScreenViewmodel = koinViewModel()) {
    val state by vm.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        if (state !is HomeScreenViewmodel.State.Loaded) vm.load()
    }

    AnimatedContent(state) { state ->
        when (state) {
            is HomeScreenViewmodel.State.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.size(64.dp))
                }
            }

            is HomeScreenViewmodel.State.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("An error has occurred.")
                }
            }

            is HomeScreenViewmodel.State.Loaded -> {
                HomeScreenContent(contentPadding, state.projects)
            }
        }
    }
}

@Composable
private fun HomeScreenContent(contentPadding: PaddingValues, projects: List<Project>) {
    LazyColumn {
        items(projects) { project ->
            Text(project.name)
        }
    }
}
