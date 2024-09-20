import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

fun main() = application {
    val state = rememberWindowState()
    val icon = rememberVectorPainter(Icons.Default.Web)
    val trayState = rememberTrayState()

    Window(
        state = state,
        title = "midilink",
        icon = icon,
        onCloseRequest = ::exitApplication,
        undecorated = true,
        transparent = true
    ) {
        window.minimumSize = Dimension(800, 400)

        App()
        WindowDraggableArea(Modifier.fillMaxWidth().height(16.dp)) {
            Text("drag")
        }
    }
}