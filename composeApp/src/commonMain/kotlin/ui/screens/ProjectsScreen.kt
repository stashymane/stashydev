package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stashy.home.Res
import dev.stashy.home.block_projects_1k
import icons.Icons
import icons.outlinelarge.Cases
import ui.locals.LocalScaffoldPadding
import ui.preview.DevicePreview
import ui.preview.PreviewHost

@Composable
fun ProjectsScreen() {
    Box(Modifier.fillMaxSize().padding(LocalScaffoldPadding.current)) {
        _root_ide_package_.ui.components.ScreenBackground(
            Res.drawable.block_projects_1k,
            Modifier.fillMaxWidth().height(300.dp)
        )

        Column {
            _root_ide_package_.ui.components.nav.NavBar {
                Icon(
                    Icons.OutlineLarge.Cases,
                    null,
                    Modifier.size(42.dp)
                )
                Text("Projects", style = MaterialTheme.typography.headlineLarge)
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
