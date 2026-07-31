package ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.darken
import icons.Icons
import icons.outline.ArrowOutwardThick
import io.ktor.http.Url
import model.display
import model.getIcon
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.inDp

@Composable
fun LinkButton(
    url: Url,
    modifier: Modifier = Modifier,
    prefixIcon: Boolean = true,
    color: Color = LocalContentColor.current,
    hoverContainerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    hoverContentColor: Color = MaterialTheme.colorScheme.inversePrimary
) {
    val uriHandler = LocalUriHandler.current
    val lineHeightDp = LocalTextStyle.current.lineHeight.inDp()

    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val pressed by interactionSource.collectIsPressedAsState()

    val contentColor by animateColorAsState(
        if (hovered || pressed) hoverContentColor else color,
        MaterialTheme.motionScheme.fastEffectsSpec()
    )
    val containerColor by animateColorAsState(
        if (pressed) hoverContainerColor.darken(1.25f) else if (hovered) hoverContainerColor else Color.Transparent,
        MaterialTheme.motionScheme.fastEffectsSpec()
    )
    val hoverProgress by animateFloatAsState(
        if (hovered) 1f else 0f,
        MaterialTheme.motionScheme.fastEffectsSpec()
    )
    val pressProgress by animateFloatAsState(
        if (pressed) 1f else 0f,
        MaterialTheme.motionScheme.fastEffectsSpec()
    )

    Box {
        Box(
            Modifier.matchParentSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                Modifier.width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .sizeIn(maxWidth = lineHeightDp, maxHeight = lineHeightDp)
                    .graphicsLayer {
                        alpha = hoverProgress
                        clip = false

                        val hoverOffset = size.width * hoverProgress
                        val clickedOffset = size.width / 4 * pressProgress
                        translationX = hoverOffset + clickedOffset
                    }
                    .background(containerColor)
                    .padding(3.dp)
            ) {
                CompositionLocalProvider(LocalContentColor provides hoverContentColor) {
                    Icon(
                        Icons.Outline.ArrowOutwardThick,
                        null,
                        Modifier.aspectRatio(1f).fillMaxSize()
                    )
                }
            }
        }

        Row(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button
            ) { uriHandler.openUri(url.toString()) }
                .pointerHoverIcon(PointerIcon.Hand)
                .background(containerColor)
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                if (prefixIcon)
                    InlineIcon(url.getIcon(), null)
                Text(
                    url.display(),
                    textDecoration = Underline,
                    color = LocalContentColor.current
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun LinkButtonPreview() = PreviewHost {
    val links = listOf(
        Url("https://google.com"),
        Url("https://github.com/stashymane"),
        Url("https://stashy.dev"),
        Url("mailto:contact@stashy.dev")
    );

    Column {
        links.forEach { link ->
            LinkButton(link)
        }
    }
}
