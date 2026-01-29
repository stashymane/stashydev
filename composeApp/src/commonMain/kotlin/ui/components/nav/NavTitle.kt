package ui.components.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ui.theme.navTitleSharedElement

@Composable
fun NavTitle(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    ProvideTextStyle(MaterialTheme.typography.headlineLarge) {
        Row(
            modifier = modifier.navTitleSharedElement("title-$text"),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, Modifier.size(42.dp))

            Text(text)
        }
    }
}
