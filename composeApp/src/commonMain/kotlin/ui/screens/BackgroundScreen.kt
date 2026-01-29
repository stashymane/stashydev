package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.theme.glorp

@Composable
fun BackgroundScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest).glorp()
    )
}
