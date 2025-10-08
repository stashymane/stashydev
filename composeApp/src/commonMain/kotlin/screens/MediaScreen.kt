package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.ScreenBackground
import components.nav.NavBar
import dev.stashy.home.Res
import dev.stashy.home.block_media_1k
import icons.Icons
import icons.outlinelarge.FitScreen

@Composable
fun MediaScreen(contentPadding: PaddingValues) {
    ScreenBackground(Res.drawable.block_media_1k, Modifier.fillMaxWidth().height(300.dp))

    Box(Modifier.fillMaxSize().padding(contentPadding)) {
        Column {
            NavBar {
                Icon(Icons.OutlineLarge.FitScreen, null, Modifier.size(42.dp))
                Text("Media", style = MaterialTheme.typography.headlineLarge)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("todo")
            }
        }
    }
}
