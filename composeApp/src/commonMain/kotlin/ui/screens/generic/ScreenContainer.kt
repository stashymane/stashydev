package ui.screens.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.theme.appWidth
import ui.theme.navBlockSharedBounds

@Composable
fun ScreenContainer(
    name: String,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        Modifier.navBlockSharedBounds(name)
            .background(MaterialTheme.colorScheme.background)
            .widthIn(max = appWidth())
            .fillMaxHeight()
    ) {
        content()
    }
}
