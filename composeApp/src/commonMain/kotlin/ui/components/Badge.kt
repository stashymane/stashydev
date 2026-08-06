package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outline.Briefcases
import icons.outlinelarge.FitScreen
import ui.preview.ComponentPreview
import ui.preview.PreviewHost

@Composable
fun Badge(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    content: @Composable () -> Unit,
) {
    val textStyle = textStyle.copy(lineHeightStyle = LineHeightStyle.Default.copy(
        trim = LineHeightStyle.Trim.Both
    ))

    ProvideTextStyle(textStyle) {
        Surface(modifier, color = containerColor, contentColor = contentColor) {
            Row(
                Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BadgePreview() = PreviewHost {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Badge(
            content = {
                InlineIcon(Icons.Outline.Briefcases, null)
                Text("Badge")
            }
        )
    }
}
