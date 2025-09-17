package components.nav

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import icons.ArrowOutwardThick
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import theme.fastBezier

private val iconEnter = fadeIn(fastBezier(200)) + slideIn(fastBezier(200)) { IntOffset(-3, 3) }
private val iconExit = fadeOut(fastBezier(200)) + slideOut(fastBezier(200)) { IntOffset(-3, 3) }

@Composable
fun NavLink(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    backgroundColor: Color = Color.Transparent,
    isActive: Boolean,
    isExternal: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(if (isActive) LocalContentColor.current.copy(alpha = 0.2f) else backgroundColor)
    val borderColor by animateColorAsState(if (isActive) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) else Color.Transparent)

    LaunchedEffect(interactionSource) {
        interactionSource.interactions
            .filterIsInstance<HoverInteraction.Enter>()
            .onEach {
                //play sound
            }.collect()
    }

    Surface(
        onClick,
        modifier = modifier.width(IntrinsicSize.Max),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        interactionSource = interactionSource
    ) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                icon()
                title()
            }
            if (isExternal) {
                AnimatedVisibility(
                    isHovered || isPressed,
                    modifier = Modifier.align(Alignment.TopEnd),
                    enter = iconEnter,
                    exit = iconExit
                ) {
                    Icon(
                        icons.Icons.ArrowOutwardThick,
                        null,
                        Modifier.padding(2.dp).size(12.dp),
                        tint = LocalContentColor.current.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialIcon(url: String, icon: ImageVector, tooltip: String) {
    val uriHandler = LocalUriHandler.current

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Below, 8.dp),
        tooltip = {
            PlainTooltip(shape = RectangleShape) {
                Text(tooltip)
            }
        },
        state = rememberTooltipState()
    ) {
        NavLink(
            icon = { Icon(icon, null, Modifier.size(24.dp)) },
            backgroundColor = MaterialTheme.colorScheme.surface,
            isActive = false,
            isExternal = true
        ) { uriHandler.openUri(url) }
    }
}
