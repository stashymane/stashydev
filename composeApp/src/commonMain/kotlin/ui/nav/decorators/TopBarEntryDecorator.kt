package ui.nav.decorators

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import ui.LocalScaffoldPadding
import ui.components.nav.NavBar

class TopBarEntryDecorator<T : Any> : NavEntryDecorator<T>(
    decorate = { entry ->
        val showTopbar = entry.metadata[MetadataKey] ?: false
        if (showTopbar) {
            Scaffold(
                topBar = { NavBar() },
                containerColor = Color.Transparent
            ) {
                CompositionLocalProvider(LocalScaffoldPadding provides it) {
                    entry.Content()
                }
            }
        } else entry.Content()
    }
) {
    object MetadataKey : NavMetadataKey<Boolean>
}
