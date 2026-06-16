package ui.screens

import Project
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outlinelarge.Cases
import kotlinx.datetime.LocalDate
import ui.components.nav.NavBar
import ui.components.nav.NavTitle
import ui.components.project.ProjectCard
import ui.preview.DevicePreview
import ui.preview.PreviewHost
import ui.screens.generic.ScreenContent
import ui.screens.generic.ScreenHost

@Composable
fun ProjectsScreen() = ScreenHost {
//        ScreenBackground(
//            Res.drawable.block_projects_1k,
//            Modifier.fillMaxWidth().height(300.dp)
//        )

    ScreenContent("projects") {
        Column {
            NavBar {
                NavTitle(Icons.OutlineLarge.Cases, "Projects")
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ProjectCard(project, Modifier.fillMaxWidth())
                ProjectCard(project, Modifier.fillMaxWidth())
                ProjectCard(project, Modifier.fillMaxWidth())
            }
        }
    }
}

val project = Project(
    name = "vintage-story-runner",
    description = "Docker image to download & run a Vintage Story server (x64 & arm64)",
    status = Unmaintained,
    urls = listOf("https://github.com/stashymane/vintage-story-runner"),
    created = LocalDate(2026, 1, 1),
    languages = listOf(Kotlin)
)

@DevicePreview
@Composable
private fun ProjectScreenPreview() = PreviewHost {
    ProjectsScreen()
}
