package ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.logos.GitHub
import icons.outline.ArrowOutwardThick
import icons.outline.CaptivePortal
import io.ktor.http.*
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.inDp

@Composable
fun LinkButton(
    url: Url,
    prefixIcon: Boolean = true,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    hoverColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    val uriHandler = LocalUriHandler.current
    val iconSize = LocalTextStyle.current.lineHeight.inDp() * 0.75f

    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()

    val decoration: TextDecoration by derivedStateOf { if (hovered) Underline else None }
    val currentColor by animateColorAsState(if (hovered) hoverColor else color)
    val hoverProgress by animateFloatAsState(
        if (hovered) 1f else 0f,
        MaterialTheme.motionScheme.fastEffectsSpec()
    )

    Box {
        Row(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button
            ) { uriHandler.openUri(url.toString()) }
                .pointerHoverIcon(PointerIcon.Hand)
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides currentColor) {
                if (prefixIcon)
                    Icon(url.icon(), null, Modifier.size(iconSize))
                Text(
                    "${url.host}${url.encodedPathAndQuery}",
                    textDecoration = decoration,
                    color = LocalContentColor.current
                )
            }
        }

        Box(
            Modifier
                .size(0.dp)
                .wrapContentSize(unbounded = true)
                .align(CenterEnd)
                .offset(x = 16.dp)
                .graphicsLayer {
                    alpha = hoverProgress
                    clip = false

                    val offset = 4.dp.toPx()
                    translationX = offset * hoverProgress
                    translationY = offset - offset * hoverProgress
                }
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(4.dp)
        ) {
            Icon(Icons.Outline.ArrowOutwardThick, null, Modifier.size(16.dp))
        }
    }
}

private fun Url.icon(): ImageVector = when (this.host) {
    "github.com" -> Icons.Logos.GitHub
    else -> Icons.Outline.CaptivePortal
}

@ComponentPreview
@Composable
private fun LinkButtonPreview() = PreviewHost {
    val links = listOf(Url("https://google.com"), Url("https://github.com/stashymane"), Url("https://stashy.dev"));

    Column {
        links.forEach { link ->
            LinkButton(link)
        }
    }
}
