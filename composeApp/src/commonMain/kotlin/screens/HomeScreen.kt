package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import components.ResponsiveRow
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
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxSize().verticalScroll(scrollState), contentAlignment = Alignment.Center) {
        Column(
            Modifier.widthIn(max = WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp).padding(contentPadding)
                .padding(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SiteHeader()

            ResponsiveRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                arrangement = Arrangement.spacedBy(16.dp)
            ) {
                val blockModifier =
                    responsive(onRow = { Modifier.weight(1f) }, onColumn = { Modifier.fillMaxWidth() })

                NavBlock(
                    blockModifier,
                    onClick = {},
                    icon = Icons.OutlineLarge.Cases,
                    text = "Projects",
                    background = {})
                NavBlock(
                    blockModifier,
                    onClick = {},
                    icon = Icons.OutlineLarge.FitScreen,
                    text = "Media",
                    background = {})
                NavBlock(
                    blockModifier,
                    onClick = {},
                    icon = Icons.OutlineLarge.UserSearch,
                    text = "About",
                    background = {})
            }

            SiteFooter()
        }
    }
}
