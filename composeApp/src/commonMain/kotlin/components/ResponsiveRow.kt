package components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass

@Composable
fun ResponsiveRow(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.HorizontalOrVertical? = null,
    content: @Composable ResponsiveScope.() -> Unit
) {
    val sizeClass = currentWindowAdaptiveInfo().windowSizeClass

    if (sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) {
        Row(modifier, arrangement ?: Arrangement.Start) {
            content.invoke(ResponsiveScope.Row(this))
        }
    } else {
        Column(modifier, arrangement ?: Arrangement.Top) {
            content.invoke(ResponsiveScope.Column(this))
        }
    }
}

sealed interface ResponsiveScope {
    class Row(scope: RowScope) : RowScope by scope, ResponsiveScope
    class Column(scope: ColumnScope) : ColumnScope by scope, ResponsiveScope

    @Composable
    fun <T> responsive(onRow: @Composable RowScope.() -> T, onColumn: @Composable ColumnScope.() -> T) =
        when (this) {
            is Row -> onRow(this)
            is Column -> onColumn(this)
        }
}
