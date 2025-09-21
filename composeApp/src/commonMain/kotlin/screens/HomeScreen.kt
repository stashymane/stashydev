package screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import components.ResponsiveRow
import components.SiteFooter
import components.SiteHeader
import components.nav.NavBlock
import dev.stashy.home.Res
import dev.stashy.home.block_about_1k
import dev.stashy.home.block_media_1k
import dev.stashy.home.block_projects_1k
import icons.Icons
import icons.outlinelarge.Cases
import icons.outlinelarge.FitScreen
import icons.outlinelarge.UserSearch
import locals.LocalBackStack
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import theme.glorp

@OptIn(ExperimentalResourceApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(contentPadding: PaddingValues) {
    val backStack = LocalBackStack.current
    val scrollState = rememberScrollState()

    val logoAnimation = remember { Animatable(0f) }
    var middleVisible by remember { mutableStateOf(false) }

    LaunchedEffect("launch animation") {
        logoAnimation.animateTo(1f, tween(1000, 300))
        middleVisible = true
    }

    Box(Modifier.fillMaxSize()) {
        Box(Modifier.matchParentSize().graphicsLayer { alpha = logoAnimation.value }.glorp())

        Box(Modifier.fillMaxSize().verticalScroll(scrollState), contentAlignment = Alignment.Center) {
            Column(
                Modifier.widthIn(max = WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp).padding(contentPadding)
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SiteHeader(Modifier.graphicsLayer { alpha = logoAnimation.value })

                AnimatedVisibility(
                    middleVisible,
                    enter = fadeIn() + expandVertically()
                ) {
                    ResponsiveRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        arrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val blockModifier =
                            responsive(onRow = { Modifier.weight(1f) }, onColumn = { Modifier.fillMaxWidth() })

                        NavBlock(
                            blockModifier,
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
                            blockModifier,
                            onClick = {},
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
                            blockModifier,
                            onClick = {},
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

                SiteFooter(
                    Modifier.graphicsLayer {
                        alpha = logoAnimation.value
                    }
                )
            }
        }

        VerticalScrollbar(
            rememberScrollbarAdapter(scrollState),
            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(2.dp)
        )
    }
}
