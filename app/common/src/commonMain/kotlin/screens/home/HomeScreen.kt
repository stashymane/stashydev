package screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import icons.Icons
import icons.logos.GitHub
import icons.logos.SoundCloud
import icons.logos.Twitter
import icons.logos.YouTube
import model.Links
import model.getIcon
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import screens.home.components.SiteFooter
import ui.LocalBackStack
import ui.LocalScaffoldPadding
import ui.components.nav.NavBlock
import ui.components.nav.SocialIcon
import ui.preview.DevicePreview
import ui.preview.PreviewHost
import ui.theme.ContainerSize
import ui.theme.currentContainerSize

@OptIn(ExperimentalGridApi::class)
@Composable
fun HomeScreen(
    vm: HomeScreenViewmodel = koinInject()
) {
    val backStack = LocalBackStack.current
    val scrollState = rememberScrollState()

    val expanded = currentContainerSize() >= ContainerSize.Regular

    Column(
        Modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(LocalScaffoldPadding.current)
            .padding(vertical = 32.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
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
                Modifier.padding(vertical = 8.dp).gridItem(columnSpan = 1, alignment = BottomStart),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = W400,
                    letterSpacing = 0.075.em
                ),
            )

            Row(
                Modifier.gridItem(columnSpan = if (expanded) 2 else 1).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                Links.Groups.All.forEach { group ->
                    HeaderLinkSection(group.name) {
                        group.links.forEach { link ->
                            SocialIcon(
                                url = link.url,
                                icon = link.url.getIcon(),
                                tooltip = link.name
                            )
                        }
                    }
                }
            }

            vm.cards.forEach { card ->
                NavBlock(
                    Modifier.fillMaxWidth(),
                    onClick = { backStack.add(card.screen) },
                    icon = card.icon,
                    text = stringResource(card.title),
                    background = { card.background.invoke(this) })
            }
        }

        SiteFooter()
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
