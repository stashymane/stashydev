import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.stashy.standard.ui.responsive.ContainerSize
import java.awt.Dimension

fun main() = application {
    val state = rememberWindowState(size = ContainerSize.Desktop.let { size -> DpSize(size.width, size.height) })
    // loading vector outside of window breaks
    // https://youtrack.jetbrains.com/issue/CMP-10788
    // https://youtrack.jetbrains.com/issue/CMP-10998
    // val icon = rememberVectorPainter(Icons.Outline.Briefcase)

    Window(onCloseRequest = ::exitApplication, state = state, title = "stashydev") {
        with(LocalDensity.current) {
            window.minimumSize =
                ContainerSize.ExtraSmall.let { Dimension(it.width.roundToPx(), it.height.roundToPx()) }
        }

        App()
    }
}
