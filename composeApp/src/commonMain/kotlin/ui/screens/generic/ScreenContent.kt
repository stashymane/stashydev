package ui.screens.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.LocalScaffoldPadding
import ui.components.VerticalScrollbar

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Box {
        Column(modifier.verticalScroll(scrollState).padding(LocalScaffoldPadding.current)) {
            content()
        }

        VerticalScrollbar(scrollState, Modifier.align(Alignment.TopEnd).padding(LocalScaffoldPadding.current))
    }
}
