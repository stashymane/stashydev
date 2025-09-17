package theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle

@Composable
fun AppTheme(
    seedColor: Color,
    isDark: Boolean,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    content: @Composable () -> Unit
) =
    DynamicMaterialTheme(
        seedColor,
        isDark,
        style = style,
        animate = true,
        typography = AppTypography(),
        content = content
    )
