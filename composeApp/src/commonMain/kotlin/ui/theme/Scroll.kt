package ui.theme

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun scrollbarStyle() = ScrollbarStyle(
    minimalHeight = 32.dp,
    thickness = 8.dp,
    shape = RectangleShape,
    hoverDurationMillis = 300,
    unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f)
)
