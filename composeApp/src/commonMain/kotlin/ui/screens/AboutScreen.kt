package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stashy.home.Res
import dev.stashy.home.block_about_1k
import icons.Icons
import icons.outlinelarge.UserSearch
import ui.components.ScreenBackground
import ui.components.nav.NavBar
import ui.locals.LocalScaffoldPadding
import ui.preview.DevicePreview
import ui.preview.PreviewHost

@Composable
fun AboutScreen() {
    Box(Modifier.fillMaxSize().padding(LocalScaffoldPadding.current)) {
        ScreenBackground(
            Res.drawable.block_about_1k,
            Modifier.fillMaxWidth().height(300.dp)
        )

        Column {
            NavBar {
                Icon(
                    Icons.OutlineLarge.UserSearch,
                    null,
                    Modifier.size(42.dp)
                )
                Text("About", style = MaterialTheme.typography.headlineLarge)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("todo")
            }
        }
    }
}

@DevicePreview
@Composable
private fun AboutScreenPreview() = PreviewHost {
    AboutScreen()
}
