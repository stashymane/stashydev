package components.nav

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import icons.ArrowOutwardThick
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach

@Composable
fun NavLink(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    isActive: Boolean,
    isExternal: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor by animateColorAsState(if (isActive) LocalContentColor.current.copy(alpha = 0.2f) else Color.Transparent)
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
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        interactionSource = interactionSource
    ) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                icon()
                title()
            }
            if (isExternal) {
                AnimatedVisibility(
                    isHovered,
                    modifier = Modifier.align(Alignment.TopEnd),
                    enter = fadeIn() + slideIn { IntOffset(-5, 5) },
                    exit = fadeOut() + slideOut { IntOffset(-5, 5) }
                ) {
                    Icon(
                        icons.Icons.ArrowOutwardThick,
                        null,
                        Modifier.padding(4.dp).size(12.dp),
                        tint = LocalContentColor.current.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
