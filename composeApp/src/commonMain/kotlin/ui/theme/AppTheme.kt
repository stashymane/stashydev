package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import model.isDark
import ui.LocalSettings

@Composable
fun AppTheme(
    color: Color,
    isDark: Boolean = LocalSettings.current.themeMode.isDark(),
    style: PaletteStyle = PaletteStyle.TonalSpot,
    animate: Boolean = true,
    content: @Composable () -> Unit
) =
    DynamicMaterialTheme(
        color,
        isDark,
        style = style,
        animate = animate,
        typography = appTypography(),
        content = content
    )
