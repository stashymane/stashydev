package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outlinelarge.Cases

@Composable
fun ProjectsScreen(contentPadding: PaddingValues) {
    Box(Modifier.fillMaxSize()) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.OutlineLarge.Cases, null, Modifier.size(42.dp))
            Text("Projects", style = MaterialTheme.typography.headlineLarge)
        }
    }
}
