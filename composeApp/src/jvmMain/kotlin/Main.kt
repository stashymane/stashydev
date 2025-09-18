import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import icons.Briefcase
import icons.Icons
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() = application {
    startKoin {
        modules(KoinModule)
    }

    val state = rememberWindowState(size = DpSize(1000.dp, 800.dp))
    val icon = rememberVectorPainter(Icons.Briefcase)

    Window(onCloseRequest = ::exitApplication, state = state, title = "stashydev", icon = icon) {
        window.minimumSize = Dimension(400, 400)

        App()
    }
}
