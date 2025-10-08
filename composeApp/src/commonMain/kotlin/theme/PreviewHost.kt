package theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
annotation class AppPreview()

@Composable
fun PreviewHost(content: @Composable () -> Unit) {
    AppTheme(Color.Blue, true) {
        content()
    }
}
