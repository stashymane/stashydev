import androidx.compose.ui.window.ComposeViewport

fun main() {
    ComposeViewport(
        configure = {
            isClearFocusOnMouseDownEnabled = true
            enableBrowserWindowInsets = true
        }
    ) {
        App()
    }
}
