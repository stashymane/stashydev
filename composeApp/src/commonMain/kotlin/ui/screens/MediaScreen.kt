package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stashy.home.Res
import dev.stashy.home.block_media_1k
import icons.Icons
import icons.outlinelarge.FitScreen
import ui.components.ScreenBackground
import ui.components.nav.NavBar
import ui.components.nav.NavTitle
import ui.locals.LocalScaffoldPadding
import ui.screens.generic.ScreenContainer
import ui.screens.generic.ScreenHost

@Composable
fun MediaScreen() = ScreenHost {
    ScreenContainer("media") {
        ScreenBackground(
            Res.drawable.block_media_1k,
            Modifier.fillMaxWidth().height(300.dp)
        )

        Box(Modifier.fillMaxSize().padding(LocalScaffoldPadding.current)) {
            Column {
                NavBar {
                    NavTitle(Icons.OutlineLarge.FitScreen, "Media")
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("todo")
                }
            }
        }
    }
}
