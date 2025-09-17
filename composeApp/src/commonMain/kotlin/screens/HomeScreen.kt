package screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.SiteFooter
import components.SiteHeader
import components.nav.NavBlock
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(contentPadding: PaddingValues) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier.widthIn(max = 900.dp).heightIn(max = 720.dp).padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SiteHeader()
            Row(
                Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NavBlock(
                    Modifier.weight(1f),
                    onClick = {},
                    icon = Icons.OutlineLarge.Cases,
                    text = "Projects",
                    background = {})
                NavBlock(
                    Modifier.weight(1f),
                    onClick = {},
                    icon = Icons.OutlineLarge.FitScreen,
                    text = "Media",
                    background = {})
                NavBlock(
                    Modifier.weight(1f),
                    onClick = {},
                    icon = Icons.OutlineLarge.UserSearch,
                    text = "About",
                    background = {})
            }
            SiteFooter()
        }
    }
}
