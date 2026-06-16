package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.filled.ArrowBack
import icons.filled.Mail
import icons.logos.GitHub
import icons.logos.SoundCloud
import icons.logos.Twitter
import icons.logos.YouTube
import ui.components.nav.SocialIcon
import ui.locals.LocalBackStack
import ui.preview.ComponentPreview
import ui.preview.PreviewHost

@Composable
private fun LinkDivider() {
    VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
}

@Composable
fun SiteHeader(
    modifier: Modifier = Modifier,
    showLinks: Boolean = true
) {
    val backStack = LocalBackStack.current

    FlowRow(
        Modifier
            .padding(horizontal = 16.dp)
            .heightIn(min = 64.dp)
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedVisibility(
                visible = backStack.isNotEmpty()
            ) {
                IconButton({ backStack.removeLast() }, enabled = backStack.isNotEmpty()) {
                    Icon(ArrowBack, "Back")
                }
            }

            Text(
                "stashymane",
                style = MaterialTheme.typography.displaySmall,
            )
        }

        AnimatedVisibility(visible = showLinks) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)) {
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
        }
    }
}

@Composable
fun HeaderLinkSection(title: String, content: @Composable () -> Unit) {
    Column(
        Modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.weight(1f).fillMaxHeight().dottedLine())
            Text(title, style = MaterialTheme.typography.labelSmall)
        }

        Row(
            Modifier.height(IntrinsicSize.Min)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
        ) {
            content()
        }
    }
}

@ComponentPreview
@Composable
private fun SiteHeaderPreview() = PreviewHost {
    SiteHeader()
}
