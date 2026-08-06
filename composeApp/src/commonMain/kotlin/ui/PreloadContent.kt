package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import data.ProjectsRepository
import org.koin.compose.koinInject

@Composable
fun PreloadContent(
    projects: ProjectsRepository = koinInject()
) {
    LaunchedEffect(projects) {
        runCatching { projects.preload() }.onFailure { it.printStackTrace() }
    }
}
