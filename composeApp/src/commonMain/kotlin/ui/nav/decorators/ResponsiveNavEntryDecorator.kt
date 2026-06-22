package ui.nav.decorators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get

class ResponsiveNavEntryDecorator<T : Any> : NavEntryDecorator<T>(
    decorate = { entry ->
        val width = entry.metadata[MetadataKey]
        width?.let { width ->
            Box(Modifier.widthIn(max = width)) {
                entry.Content()
            }
        } ?: entry.Content()
    }
) {
    object MetadataKey : NavMetadataKey<Dp>
}
