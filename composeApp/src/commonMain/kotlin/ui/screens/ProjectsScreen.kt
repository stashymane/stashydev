package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stashy.home.Res
import dev.stashy.home.block_projects_1k
import icons.Icons
import icons.outlinelarge.Cases
import ui.components.ScreenBackground
import ui.components.nav.NavBar
import ui.components.nav.NavTitle
import ui.preview.DevicePreview
import ui.preview.PreviewHost
import ui.screens.generic.ScreenContainer
import ui.screens.generic.ScreenHost

@Composable
fun ProjectsScreen() = ScreenHost {
    ScreenContainer("projects") {
        ScreenBackground(
            Res.drawable.block_projects_1k,
            Modifier.fillMaxWidth().height(300.dp)
        )

        Column {
            NavBar {
                NavTitle(Icons.OutlineLarge.Cases, "Projects")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("todo")
            }
        }
    }
}

@DevicePreview
@Composable
private fun ProjectScreenPreview() = PreviewHost {
    ProjectsScreen()
}
