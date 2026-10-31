import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.v2.Window
import androidx.compose.ui.window.v2.WindowBoundsProvider
import androidx.compose.ui.window.v2.WindowPositionProvider
import androidx.compose.ui.window.v2.rememberWindowState
import dev.stashy.standard.ui.responsive.ContainerSize

private val ContainerSize.dpSize: DpSize get() = DpSize(width, height)

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    // loading vector outside of window breaks
    // https://youtrack.jetbrains.com/issue/CMP-10788
    // https://youtrack.jetbrains.com/issue/CMP-10998
    // val icon = rememberVectorPainter(Icons.Outline.Briefcase)

    val state = rememberWindowState(
        initialBoundsProvider = WindowBoundsProvider(
            sizeProvider = { contentToWindowSize(ContainerSize.Desktop.dpSize) },
            positionProvider = WindowPositionProvider.CenteredInScreenBounds,
        )
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "stashydev",
        minSize = ContainerSize.ExtraSmall.dpSize,
    ) {
        App()
    }
}
