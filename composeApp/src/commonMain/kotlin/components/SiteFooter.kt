package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.Icons
import icons.outlinelarge.CaptivePortal
import preview.ComponentPreview

@Composable
fun SiteFooter(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.height(2.dp).weight(1f).dottedLine())
        Text("worldwide", style = MaterialTheme.typography.labelSmall)
        Icon(Icons.OutlineLarge.CaptivePortal, contentDescription = null)
        Spacer(Modifier.height(2.dp).weight(0.05f).dottedLine())
    }
}

@Composable
@ComponentPreview
private fun SiteFooterPreview() {
    SiteFooter()
}
