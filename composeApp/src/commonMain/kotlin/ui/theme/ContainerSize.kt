package ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import kotlinx.serialization.Serializable

@Serializable
enum class ContainerSize(val value: Dp) {
    Wide(WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND.dp),
    Regular(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp),
    Small(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND.dp)
}
