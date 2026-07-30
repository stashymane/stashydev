package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import model.isDark
import ui.LocalSettings

val LocalAppTypography = staticCompositionLocalOf<Typography?> { null }

@Composable
fun AppTheme(
    color: Color,
    isDark: Boolean = LocalSettings.current.themeMode.isDark(),
    style: PaletteStyle = PaletteStyle.TonalSpot,
    animate: Boolean = true,
    typography: Typography? = null,
    content: @Composable () -> Unit
) {
    val resolvedTypography =
        typography ?: LocalAppTypography.current ?: appTypography(MaterialTheme.typography)

    CompositionLocalProvider(LocalAppTypography provides resolvedTypography) {
        DynamicMaterialTheme(
            color,
            isDark,
            style = style,
            animate = animate,
            typography = resolvedTypography,
            content = content
        )
    }
}
