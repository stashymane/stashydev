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
import dev.stashy.home.block_about_1k
import icons.Icons
import icons.outlinelarge.UserSearch

@Composable
fun AboutScreen(contentPadding: PaddingValues) {
    Box(Modifier.fillMaxSize().padding(contentPadding)) {
        ScreenBackground(Res.drawable.block_about_1k, Modifier.fillMaxWidth().height(300.dp))

        Column {
            NavBar {
                Icon(Icons.OutlineLarge.UserSearch, null, Modifier.size(42.dp))
                Text("About", style = MaterialTheme.typography.headlineLarge)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("todo")
            }
        }
    }
}
