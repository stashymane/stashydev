package model

import androidx.compose.ui.graphics.vector.ImageVector
import icons.Icons
import icons.filled.Mail
import icons.logos.GitHub
import icons.logos.SoundCloud
import icons.logos.Twitter
import icons.logos.YouTube
import icons.outline.CaptivePortal
import io.ktor.http.*

fun Url.getIcon(): ImageVector = when (this.host) {
    "github.com" -> Icons.Logos.GitHub
    "soundcloud.com" -> Icons.Logos.SoundCloud
    "youtube.com" -> Icons.Logos.YouTube
    "x.com" -> Icons.Logos.Twitter
    else if protocol.name == "mailto" -> Icons.Filled.Mail
    else -> Icons.Outline.CaptivePortal
}
