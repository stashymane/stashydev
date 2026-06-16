package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.stashy.home.Res
import dev.stashy.home.block_about_1k
import dev.stashy.home.block_media_1k
import dev.stashy.home.block_projects_1k
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import model.Screen
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.koinInject
import ui.components.ResponsiveRow
import ui.components.nav.NavBlock
import ui.locals.LocalBackStack
import ui.locals.LocalScaffoldPadding
import ui.preview.DevicePreview
import ui.preview.PreviewHost
import ui.theme.appWidth
import ui.theme.navBlockSharedBounds
import ui.vm.HomeScreenViewmodel

@Composable
fun HomeScreen(
    vm: HomeScreenViewmodel = koinInject()
) {
    val backStack = LocalBackStack.current
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize().verticalScroll(scrollState),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.widthIn(max = appWidth())
                    .padding(LocalScaffoldPadding.current)
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ResponsiveRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    arrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val blockModifier =
                        responsive(
                            onRow = { Modifier.weight(1f) },
                            onColumn = { Modifier.fillMaxWidth() })

                    NavBlock(
                        blockModifier.navBlockSharedBounds("projects"),
                        onClick = { backStack.add(Screen.Projects) },
                        icon = Icons.OutlineLarge.Cases,
                        text = "Projects",
                        background = {
                            Image(
                                imageResource(Res.drawable.block_projects_1k),
                                null,
                                it,
                                contentScale = ContentScale.Crop
                            )
                        })
                    NavBlock(
                        blockModifier.navBlockSharedBounds("media"),
                        onClick = { backStack.add(Screen.Media) },
                        icon = Icons.OutlineLarge.FitScreen,
                        text = "Media",
                        background = {
                            Image(
                                imageResource(Res.drawable.block_media_1k),
                                null,
                                it,
                                contentScale = ContentScale.Crop
                            )
                        })
                    NavBlock(
                        blockModifier.navBlockSharedBounds("about"),
                        onClick = { backStack.add(Screen.About) },
                        icon = Icons.OutlineLarge.UserSearch,
                        text = "About",
                        background = {
                            Image(
                                imageResource(Res.drawable.block_about_1k),
                                null,
                                it,
                                contentScale = ContentScale.Crop
                            )
                        })
                }
            }
        }

//        VerticalScrollbar(
//            rememberScrollbarAdapter(scrollState),
//            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(2.dp)
//        )
    }
}

@DevicePreview
@Composable
private fun HomeScreenPreview() = PreviewHost {
    HomeScreen(HomeScreenViewmodel())
}
