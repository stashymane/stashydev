package ui.screens

import Project
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import model.AppState
import model.ProjectState
import org.koin.compose.koinInject
import ui.components.project.ProjectCard
import ui.preview.DevicePreview
import ui.preview.PreviewData
import ui.preview.PreviewHost
import ui.screens.generic.ScreenContent


@OptIn(ExperimentalGridApi::class)
@Composable
fun ProjectsScreen(
    appState: AppState = koinInject()
) {
    val state by appState.projectState.collectAsStateWithLifecycle()

    AnimatedContent(state) {
        when (it) {
            is ProjectState.Loading -> {
                LoadingScreen {}
            }

            is ProjectState.Loaded -> ProjectScreenContent(it.projects)
            is ProjectState.Failed -> {
                Text("Failed to load projects.")
            }
        }
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
private fun ProjectScreenContent(
    projects: List<Project>
) = ScreenContent {
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val columns = if (sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
        3
    else if (sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
        2
    else
        1

    Grid(
        {
            repeat(columns) {
                column(1 / columns.toFloat())
            }

            gap(8.dp)
        },
        Modifier.padding(horizontal = 8.dp).padding(bottom = 16.dp)
    ) {
        projects.forEach {
            ProjectCard(it, Modifier.fillMaxSize())
        }
    }
}


@DevicePreview
@Composable
private fun ProjectScreenPreview() = PreviewHost {
    ProjectsScreen(
        AppState(
            projects = listOf(
                PreviewData.project, PreviewData.project, PreviewData.project,
                PreviewData.project
            )
        )
    )
}
