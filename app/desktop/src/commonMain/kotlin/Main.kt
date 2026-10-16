import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.registerSkikoComposeImplementation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.stashy.standard.ui.responsive.ContainerSize
import icons.Icons
import icons.outline.Briefcase
import java.awt.Dimension

fun main() {
    @OptIn(InternalComposeUiApi::class)
    registerSkikoComposeImplementation() //workaround: https://youtrack.jetbrains.com/issue/CMP-10788

    application {
        val state = rememberWindowState(size = ContainerSize.Desktop.let { size -> DpSize(size.width, size.height) })
        val icon = rememberVectorPainter(Icons.Outline.Briefcase)

        Window(onCloseRequest = ::exitApplication, state = state, title = "stashydev", icon = icon) {
            with(LocalDensity.current) {
                window.minimumSize =
                    ContainerSize.ExtraSmall.let { Dimension(it.width.roundToPx(), it.height.roundToPx()) }
            }

            App()
        }
    }
}
