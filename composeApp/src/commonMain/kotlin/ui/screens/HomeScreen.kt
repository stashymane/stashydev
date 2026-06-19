package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import icons.Icons
import icons.filled.Mail
import icons.logos.GitHub
import icons.logos.SoundCloud
import icons.logos.Twitter
import icons.logos.YouTube
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.koinInject
import ui.components.nav.NavBlock
import ui.components.nav.SocialIcon
import ui.locals.LocalBackStack
import ui.locals.LocalScaffoldPadding
import ui.preview.DevicePreview
import ui.preview.PreviewHost
import ui.theme.appWidth
import ui.theme.navBlockSharedBounds
import ui.vm.HomeScreenViewmodel

@OptIn(ExperimentalGridApi::class)
@Composable
fun HomeScreen(
    vm: HomeScreenViewmodel = koinInject()
) {
    val backStack = LocalBackStack.current
    val scrollState = rememberScrollState()

    val windowInfo = currentWindowAdaptiveInfoV2()
    val expanded = windowInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND)

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier.widthIn(max = appWidth())
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(LocalScaffoldPadding.current)
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Grid(
                {
                    if (expanded) {
                        column(1f / 3f)
                        column(1f / 3f)
                        column(1f / 3f)
                    } else {
                        column(1.fr)
                    }

                    gap(16.dp)
                },
                Modifier.fillMaxSize()
            ) {
                Text(
                    "stashymane",
                    Modifier.padding(vertical = 8.dp).gridItem(columnSpan = 1),
                    style = MaterialTheme.typography.displaySmall,
                )

                Row(
                    Modifier.gridItem(columnSpan = if (expanded) 2 else 1).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    HeaderLinkSection("content") {
                        SocialIcon(
                            url = "https://github.com/stashymane",
                            icon = Icons.Logos.GitHub,
                            tooltip = "GitHub"
                        )

                        LinkDivider()

                        SocialIcon(
                            url = "https://soundcloud.com/stashymane",
                            icon = Icons.Logos.SoundCloud,
                            tooltip = "SoundCloud"
                        )

                        LinkDivider()

                        SocialIcon(
                            url = "https://youtube.com/@stashymane",
                            icon = Icons.Logos.YouTube,
                            tooltip = "YouTube"
                        )
                    }

                    HeaderLinkSection("social") {
                        SocialIcon(
                            url = "https://x.com/stashyymane",
                            icon = Icons.Logos.Twitter,
                            tooltip = "X/Twitter"
                        )

                        LinkDivider()

                        SocialIcon(
                            url = "mailto:me@stashy.dev",
                            icon = Icons.Filled.Mail,
                            tooltip = "Mail"
                        )
                    }
                }

                vm.cards.forEach { card ->
                    NavBlock(
                        Modifier.navBlockSharedBounds(card.id).fillMaxWidth(),
                        onClick = { backStack.add(card.screen) },
                        icon = card.icon,
                        text = card.title,
                        background = {
                            Image(
                                imageResource(card.background),
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

@Composable
fun HeaderLinkSection(title: String, content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall)
        }

        Row(
            Modifier.border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
        ) {
            content()
        }
    }
}

@Composable
private fun LinkDivider() {
    VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
}


@DevicePreview
@Composable
private fun HomeScreenPreview() = PreviewHost {
    HomeScreen(HomeScreenViewmodel())
}
