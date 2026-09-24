package screens.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import screens.ScreenContent
import ui.components.UnderConstruction
import ui.preview.DevicePreview
import ui.preview.PreviewHost

@Composable
fun MediaScreen() {
    ScreenContent {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UnderConstruction()
        }
    }
}

@DevicePreview
@Composable
private fun MediaScreenPreview() = PreviewHost {
    MediaScreen()
}
