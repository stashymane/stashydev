package ui.screens.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.theme.navBlockSharedBounds

@Composable
fun ScreenContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    Column() {
        content()
    }
}

@Composable
fun ScreenContent(
    name: String,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .navBlockSharedBounds(name)
            .background(MaterialTheme.colorScheme.background)
    ) {
        content()
    }
}
