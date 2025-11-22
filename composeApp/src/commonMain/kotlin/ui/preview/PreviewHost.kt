package ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ui.theme.AppTheme

@Composable
fun PreviewHost(
    content: @Composable () -> Unit
) {
    AppTheme(Color.Blue, true) {
        content()
    }
}
