package components.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import canGoBack
import icons.Icons
import icons.filled.ArrowBack
import locals.LocalBackStack
import navigateBack
import theme.AppPreview
import theme.PreviewHost

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {}
) {
    val backStack = LocalBackStack.current

    Box {
        Row(
            modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton({ backStack.navigateBack() }, enabled = backStack.canGoBack()) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }

            title()
        }
    }
}

@AppPreview
@Composable
private fun NavBarPreview() = PreviewHost {
}
