package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
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
    modifier: Modifier = Modifier.fillMaxSize(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Box {
        Column(
            modifier.verticalScroll(scrollState).padding(LocalScaffoldPadding.current),
            verticalArrangement,
            horizontalAlignment
        ) {
            content()
        }

        VerticalScrollbar(
            scrollState,
            Modifier.align(Alignment.TopEnd).padding(LocalScaffoldPadding.current)
        )
    }
}
