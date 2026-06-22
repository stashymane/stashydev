package ui.screens.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.LocalScaffoldPadding

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(Modifier.padding(LocalScaffoldPadding.current).then(modifier)) {
        content()
    }
}
