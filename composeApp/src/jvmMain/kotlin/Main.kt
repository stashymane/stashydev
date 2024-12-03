import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() = application {
    startKoin {
        modules(KoinModule)
    }

    val state = rememberWindowState()
    val icon = rememberVectorPainter(Icons.Default.Web)

    Window(onCloseRequest = ::exitApplication, state = state, title = "stashydev", icon = icon) {
        window.minimumSize = Dimension(800, 400)

        App()
    }
}
