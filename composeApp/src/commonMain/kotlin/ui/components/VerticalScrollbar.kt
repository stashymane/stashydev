package ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.preview.ComponentPreview
import ui.preview.PreviewHost

private val ScrollbarWidthCollapsed = 2.dp
private val ScrollbarWidthExpanded = 6.dp

private fun computeThumb(
    trackHeight: Int,
    contentSize: Int,
    viewportSize: Int,
    scrollOffset: Int,
): Pair<Int, Int> {
    val effectiveContentSize = contentSize.takeIf { it > 0 } ?: 1
    val effectiveViewport = viewportSize.coerceAtMost(effectiveContentSize)
    val thumbHeight = (trackHeight * effectiveViewport.toFloat() / effectiveContentSize).toInt().coerceAtLeast(16)
    val scrollRange = (effectiveContentSize - effectiveViewport).coerceAtLeast(1)
    val thumbOffset = ((trackHeight - thumbHeight) * (scrollOffset.toFloat() / scrollRange))
        .toInt().coerceIn(0, trackHeight - thumbHeight)
    return thumbHeight to thumbOffset
}

@Composable
fun VerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    collapsedWidth: Dp = ScrollbarWidthCollapsed,
    expandedWidth: Dp = ScrollbarWidthExpanded,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()

    val hovered by interactionSource.collectIsHoveredAsState()
    val width by animateDpAsState(if (hovered) expandedWidth else collapsedWidth)
    val color by animateColorAsState(MaterialTheme.colorScheme.outline.copy(alpha = if (hovered) 0.4f else 0.2f))

    if (scrollState.maxValue <= 0) return

    Box(
        modifier = modifier
            .width(expandedWidth)
            .fillMaxHeight()
            .hoverable(interactionSource)
            .pointerInput(scrollState) {
                detectVerticalDragGestures { _, dragAmount ->
                    val contentSize = scrollState.maxValue + scrollState.viewportSize
                    val (thumbHeight, _) = computeThumb(
                        size.height,
                        contentSize,
                        scrollState.viewportSize,
                        scrollState.value
                    )
                    val effectiveContentSize = contentSize.takeIf { it > 0 } ?: 1
                    val effectiveViewport = scrollState.viewportSize.coerceAtMost(effectiveContentSize)
                    val scrollRange = (effectiveContentSize - effectiveViewport).coerceAtLeast(1)
                    val scrollDelta = dragAmount / (size.height - thumbHeight).coerceAtLeast(1) * scrollRange
                    scope.launch { scrollState.scrollTo((scrollState.value + scrollDelta).toInt()) }
                }
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        Layout(
            content = {
                Box(
                    Modifier
                        .width(width)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )
            }
        ) { measurables, constraints ->
            val contentSize = scrollState.maxValue + scrollState.viewportSize
            val (thumbHeight, thumbOffset) = computeThumb(
                constraints.maxHeight, contentSize, scrollState.viewportSize, scrollState.value
            )
            val thumbPlaceable = measurables[0].measure(
                constraints.copy(minHeight = thumbHeight, maxHeight = thumbHeight)
            )
            layout(constraints.maxWidth, constraints.maxHeight) {
                thumbPlaceable.placeRelative(constraints.maxWidth - thumbPlaceable.width, thumbOffset)
            }
        }
    }
}

@ComponentPreview
@Composable
private fun VerticalScrollbarPreview() = PreviewHost {
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxWidth().height(200.dp)) {
        Box(Modifier.fillMaxWidth().height(200.dp).verticalScroll(scrollState)) {
            Box(Modifier.height(1000.dp))
        }

        VerticalScrollbar(
            scrollState,
            Modifier.align(Alignment.CenterEnd).padding(2.dp),
        )
    }
}
