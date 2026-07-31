package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import icons.Icons
import icons.outline.PinDrop24Dp
import model.Links
import ui.components.InlineIcon
import ui.components.LinkButton
import ui.preview.DevicePreview
import ui.preview.PreviewHost
import ui.screens.generic.ScreenContent
import ui.theme.ContainerSize

@Composable
fun AboutScreen() = ScreenContent {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BusinessCard(Modifier.padding(vertical = 16.dp))
    }
}

@Composable
fun BusinessCard(modifier: Modifier = Modifier) {
    Surface(
        modifier,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            Modifier.padding(horizontal = 22.dp, vertical = 16.dp)
                .widthIn(max = ContainerSize.Small.value),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    "stashymane",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = Black
                )
                Text(
                    "Albertas Š.",
                    style = MaterialTheme.typography.titleLarge,
                    letterSpacing = 0.07.em,
                    fontWeight = Bold,
                    color = LocalContentColor.current.copy(alpha = 0.8f)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InlineIcon(Icons.Outline.PinDrop24Dp, "Location")
                    Text("Vilnius, Lithuania", fontWeight = Bold)
                }

                LinkButton(Links.email)
            }

            Column {
                Text("Usually software developer, often musician, sometimes other content creator.")
            }
        }
    }
}

@DevicePreview
@Composable
private fun AboutScreenPreview() = PreviewHost {
    AboutScreen()
}
