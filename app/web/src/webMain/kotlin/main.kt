import androidx.compose.ui.window.ComposeViewport
import org.jetbrains.compose.resources.configureWebResources

fun main() {
    configureWebResources {
        resourcePathMapping { path -> "/$path" }
    }

    ComposeViewport(
        configure = {
            isClearFocusOnMouseDownEnabled = true
            enableBrowserWindowInsets = true
        }
    ) {
        App()
    }
}
