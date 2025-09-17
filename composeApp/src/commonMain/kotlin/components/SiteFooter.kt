package components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outlinelarge.CaptivePortal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SiteFooter(modifier: Modifier = Modifier) {
    @Composable
    fun Modifier.line(color: Color = MaterialTheme.colorScheme.surfaceContainer) = drawBehind {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }

    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.height(2.dp).weight(1f).line())
        Text("worldwide", style = MaterialTheme.typography.labelSmall)
        Icon(Icons.OutlineLarge.CaptivePortal, contentDescription = null)
        Spacer(Modifier.height(2.dp).weight(0.05f).line())
    }
}

@Preview
@Composable
private fun SiteFooterPreview() {
    SiteFooter()
}
