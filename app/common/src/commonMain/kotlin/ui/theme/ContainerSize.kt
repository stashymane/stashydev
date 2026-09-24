package ui.theme

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import kotlinx.serialization.Serializable

@Serializable
enum class ContainerSize(val value: Dp) {
    Small(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND.dp),
    Regular(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp),
    Wide(WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND.dp),
}

@Composable
fun currentContainerSize(): ContainerSize {
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass

    return remember(sizeClass) {
        ContainerSize.entries.asReversed()
            .firstOrNull { sizeClass.isWidthAtLeastBreakpoint(it.value.value.toInt()) }
            ?: Small
    }
}
