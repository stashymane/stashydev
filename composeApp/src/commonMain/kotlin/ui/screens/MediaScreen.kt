package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ui.preview.DevicePreview
import ui.preview.PreviewHost
import ui.screens.generic.ScreenContent
import ui.screens.generic.ScreenHost

@Composable
fun MediaScreen() = ScreenHost {
    ScreenContent {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("todo")
        }
    }
}

@DevicePreview
@Composable
private fun MediaScreenPreview() = PreviewHost {
    MediaScreen()
}
