package ui.screens

import Project
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import icons.Icons
import icons.outline.ArrowOutwardThick
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
    val columns =
        if (sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
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
        Row(Modifier.gridItem(columnSpan = columns).padding(16.dp, 16.dp)) {
            Text("Featured", style = MaterialTheme.typography.headlineLarge, fontWeight = Black)
        }

        projects.forEach {
            ProjectCard(it, Modifier.fillMaxSize())
        }

        MoreProjectsButton(Modifier.fillMaxSize())
    }
}

@Composable
private fun MoreProjectsButton(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()

    Surface(
        modifier.clickable(interactionSource = interactionSource) { uriHandler.openUri("https://github.com/stashymane") }
            .pointerHoverIcon(PointerIcon.Hand),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Box(Modifier.padding(16.dp).heightIn(min = 200.dp)) {
            Column(
                Modifier.align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "More?",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Column {
                    Text("More projects are available on GitHub.")
                    Text("github.com/stashymane", textDecoration = Underline)
                }
            }

            val iconSize by animateDpAsState(if (hovered) 64.dp else 52.dp)

            Icon(
                Icons.Outline.ArrowOutwardThick,
                null,
                Modifier.align(Alignment.TopEnd).alpha(0.5f).size(iconSize)
            )
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
