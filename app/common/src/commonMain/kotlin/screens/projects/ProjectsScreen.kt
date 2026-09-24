package screens.projects

import Project
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.ProjectsRepository
import dev.stashy.data.dataSource
import icons.Icons
import icons.outline.ArrowOutwardThick
import org.koin.compose.viewmodel.koinViewModel
import screens.LoadingFailedScreen
import screens.LoadingScreen
import screens.ScreenContent
import screens.projects.components.ProjectCard
import ui.LocalContainerSize
import ui.preview.DevicePreview
import ui.preview.PreviewData
import ui.preview.PreviewHost


@OptIn(ExperimentalGridApi::class)
@Composable
fun ProjectsScreen(
    vm: ProjectsViewmodel = koinViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        vm.onLaunch()
    }

    AnimatedContent(
        state,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { state ->
        when (state) {
            is Loading -> LoadingScreen {}
            is Failed -> LoadingFailedScreen(onRetry = vm::onReload) {
                Text("Failed to load projects.")
            }

            is Success -> ProjectScreenContent(featured = state.featured, latest = state.latest)
        }
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
private fun ProjectScreenContent(
    featured: List<Project>,
    latest: List<Project>,
) = ScreenContent {
    val containerSize = LocalContainerSize.current
    val columns = when {
        containerSize >= Wide -> 3
        containerSize >= Regular -> 2
        else -> 1
    }

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

        featured.forEach {
            ProjectCard(it, Modifier.fillMaxSize())
        }

        Row(Modifier.gridItem(columnSpan = columns).padding(16.dp, 16.dp)) {
            Text("Latest", style = MaterialTheme.typography.headlineLarge, fontWeight = Black)
        }

        latest.forEach {
            ProjectCard(it, Modifier.fillMaxSize())
        }

        MoreProjectsButton(
            Modifier
                .gridItem(columnSpan = columns - latest.size % columns)
                .fillMaxSize()
        )
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
    val featured = listOf(
        PreviewData.project,
        PreviewData.project,
        PreviewData.project,
        PreviewData.project,
    )
    val latest = listOf(
        PreviewData.project,
        PreviewData.project,
        PreviewData.project,
    )
    val vm = ProjectsViewmodel(
        repo = ProjectsRepository(
            featured = dataSource { featured },
            latest = dataSource { latest },
        )
    )

    ProjectsScreen(vm)
}
