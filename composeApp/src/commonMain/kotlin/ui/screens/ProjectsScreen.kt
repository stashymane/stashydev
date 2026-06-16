package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outlinelarge.Cases
import ui.components.nav.NavBar
import ui.components.nav.NavTitle
import ui.components.project.ProjectCard
import ui.preview.DevicePreview
import ui.preview.PreviewData
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
                ProjectCard(PreviewData.project, Modifier.fillMaxWidth())
                ProjectCard(PreviewData.project, Modifier.fillMaxWidth())
                ProjectCard(PreviewData.project, Modifier.fillMaxWidth())
            }
        }
    }
}


@DevicePreview
@Composable
private fun ProjectScreenPreview() = PreviewHost {
    ProjectsScreen()
}
