package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

@Composable
fun appWidth(): Dp = WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp
