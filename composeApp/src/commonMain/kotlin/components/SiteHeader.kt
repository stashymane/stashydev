package components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import components.nav.NavLink
import icons.Icons
import icons.LogoGithub
import icons.LogoTwitter
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SiteHeader(modifier: Modifier = Modifier) {
    Row(Modifier.padding(horizontal = 16.dp).then(modifier), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "stashymane",
            style = MaterialTheme.typography.displaySmall,
        )

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SocialIcon(
                url = "https://github.com/stashymane",
                icon = Icons.LogoGithub,
                tooltip = "GitHub"
            )
            SocialIcon(
                url = "https://x.com/stashyymane",
                icon = Icons.LogoTwitter,
                tooltip = "X/Twitter"
            )
            SocialIcon(
                url = "mailto:me@stashy.dev",
                icon = androidx.compose.material.icons.Icons.Default.Mail,
                tooltip = "Mail"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialIcon(url: String, icon: ImageVector, tooltip: String) {
    val uriHandler = LocalUriHandler.current

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Below),
        tooltip = {
            PlainTooltip {
                Text(tooltip)
            }
        },
        state = rememberTooltipState()
    ) {
        NavLink(
            icon = { Icon(icon, null, Modifier.size(24.dp)) },
            isActive = false,
            isExternal = true
        ) { uriHandler.openUri(url) }
    }
}

@Preview
@Composable
private fun SiteHeaderPreview() {
    SiteHeader()
}
