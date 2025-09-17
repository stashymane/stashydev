package components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.nav.SocialIcon
import icons.Icons
import icons.LogoGithub
import icons.LogoTwitter
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SiteHeader(modifier: Modifier = Modifier) {
    FlowRow(
        Modifier.padding(horizontal = 16.dp).then(modifier),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.weight(1f)) {
            Text(
                "stashymane",
                style = MaterialTheme.typography.displaySmall,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)) {
            HeaderLinkSection("content") {
                SocialIcon(
                    url = "https://github.com/stashymane",
                    icon = Icons.LogoGithub,
                    tooltip = "GitHub"
                )
            }

            HeaderLinkSection("social") {
                SocialIcon(
                    url = "https://x.com/stashyymane",
                    icon = Icons.LogoTwitter,
                    tooltip = "X/Twitter"
                )

                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                SocialIcon(
                    url = "mailto:me@stashy.dev",
                    icon = androidx.compose.material.icons.Icons.Default.Mail,
                    tooltip = "Mail"
                )
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
        Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(
                Modifier.weight(1f).fillMaxHeight()
                    .dottedLine(width = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            )
            Text(title, style = MaterialTheme.typography.labelSmall)
        }

        Row(Modifier.height(IntrinsicSize.Min)) {
            content()
        }
    }
}

@Preview
@Composable
private fun SiteHeaderPreview() {
    SiteHeader()
}
