package ui.components.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outline.ArrowOutwardThick
import icons.outlinelarge.UserSearch
import io.ktor.http.Url
import ui.preview.ComponentPreview
import ui.preview.PreviewHost
import ui.theme.fastBezier

private val iconEnter = fadeIn(fastBezier(200)) + slideIn(
    fastBezier(200)
) { IntOffset(-3, 3) }
private val iconExit = fadeOut(fastBezier(200)) + slideOut(
    fastBezier(200)
) { IntOffset(-3, 3) }

@Composable
fun NavLink(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    isExternal: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(if (isActive) MaterialTheme.colorScheme.onSurface else Color.Transparent)
    val contentColor by animateColorAsState(if (isActive) MaterialTheme.colorScheme.surface else LocalContentColor.current)
    val borderColor by animateColorAsState(
        if (isActive) MaterialTheme.colorScheme.outline.copy(
            alpha = 0.3f
        ) else Color.Transparent
    )

    Box {
        Surface(
            onClick,
            modifier = modifier.pointerHoverIcon(PointerIcon.Hand),
            color = backgroundColor,
            contentColor = contentColor,
            border = BorderStroke(1.dp, borderColor),
            interactionSource = interactionSource
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()
                title()
            }
        }

        if (isExternal) {
            AnimatedVisibility(
                isHovered || isPressed,
                Modifier.align(Alignment.TopEnd).padding(2.dp),
                enter = iconEnter,
                exit = iconExit
            ) {
                Icon(
                    Icons.Outline.ArrowOutwardThick,
                    null,
                    Modifier.size(12.dp),
                    tint = LocalContentColor.current.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialIcon(url: Url, icon: ImageVector, tooltip: String) {
    val uriHandler = LocalUriHandler.current

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Below,
            8.dp
        ),
        tooltip = {
            PlainTooltip(
                Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                shape = RectangleShape,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = LocalContentColor.current
            ) {
                Text(tooltip)
            }
        },
        state = rememberTooltipState()
    ) {
        NavLink(
            icon = { Icon(icon, null, Modifier.size(24.dp)) },
            isActive = false,
            isExternal = true,
            title = {}
        ) { uriHandler.openUri(url.toString()) }
    }
}

@ComponentPreview
@Composable
private fun NavLinkPreview() = PreviewHost {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        NavLink(
            title = { Text("Hello") },
            icon = { Icon(Icons.OutlineLarge.UserSearch, null) },
            isActive = false,
        ) {}
        NavLink(
            title = { Text("Hello") },
            icon = { Icon(Icons.OutlineLarge.UserSearch, null) },
            isActive = true,
        ) {}
    }
}
