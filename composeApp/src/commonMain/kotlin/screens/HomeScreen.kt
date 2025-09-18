package screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
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
import org.jetbrains.skia.RuntimeShaderBuilder
import theme.Shaders

@OptIn(ExperimentalResourceApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(contentPadding: PaddingValues) {
    val backStack = LocalBackStack.current
    val scrollState = rememberScrollState()
    val backgroundEffect = remember { Shaders.Pattern }

    Box(Modifier.fillMaxSize()) {
        val time by produceState(0f) {
            while (true) {
                withInfiniteAnimationFrameMillis { value = it / 1000f }
            }
        }

        Box(Modifier.matchParentSize().drawWithCache {
            val shader = RuntimeShaderBuilder(backgroundEffect).apply {
                uniform("iTime", time)
                uniform("iResolution", size.width, size.height)
                uniform("Warp2X", 0.1f)
                uniform("Warp2Y", 0f)
                uniform("WarpSpeedX", 0f)
                uniform("WarpSpeedY", 0.1f)
                uniform("RingStrength", 2f)
            }.makeShader()
            val brush = ShaderBrush(shader)

            onDrawBehind {
                drawRect(brush)
            }
        })

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

                SiteFooter()
            }
        }

        VerticalScrollbar(
            rememberScrollbarAdapter(scrollState),
            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(2.dp)
        )
    }
}
