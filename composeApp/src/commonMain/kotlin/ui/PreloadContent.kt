package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import data.AboutRepository
import data.ProjectsRepository
import org.koin.compose.koinInject

@Composable
fun PreloadContent(
    projects: ProjectsRepository = koinInject(),
    about: AboutRepository = koinInject()
) {
    LaunchedEffect(projects) {
        projects.preload()
    }
    LaunchedEffect(about) {
        about.preload()
    }
}
