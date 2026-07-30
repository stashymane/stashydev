package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.materialkolor.LocalDynamicMaterialThemeSeed
import com.materialkolor.PaletteStyle
import com.materialkolor.ktx.animateColorScheme
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
    val typography =
        typography ?: LocalAppTypography.current ?: rememberAppTypography(MaterialTheme.typography)
    val cache = LocalThemePaletteCache.current ?: remember { ThemePaletteCache() }

    CompositionLocalProvider(
        LocalAppTypography provides typography,
        LocalThemePaletteCache provides cache,
    ) {
        val colorScheme = rememberSharedColorScheme(color, isDark, style)
        val scheme = if (animate) animateColorScheme(colorScheme) else colorScheme

        CompositionLocalProvider(LocalDynamicMaterialThemeSeed provides color) {
            MaterialTheme(
                colorScheme = scheme,
                typography = typography,
                content = content,
            )
        }
    }
}
