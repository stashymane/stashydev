package ui.nav.decorators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get

class BackgroundEntryDecorator<T : Any> : NavEntryDecorator<T>(
    decorate = { entry ->
        val color = entry.metadata[MetadataKey]?.invoke()
        color?.let { color ->
            Box(Modifier.background(color)) {
                entry.Content()
            }
        } ?: entry.Content()
    }
) {
    object MetadataKey : NavMetadataKey<@Composable () -> Color>
}
