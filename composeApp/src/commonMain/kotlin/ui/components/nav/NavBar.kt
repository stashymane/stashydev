package ui.components.nav

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.preview.ComponentPreview
import ui.preview.PreviewHost

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {}
) {
    Box {
        Row(
            modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            title()
        }
    }
}

@ComponentPreview
@Composable
private fun NavBarPreview() = PreviewHost {
}
