package ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

@Immutable
data class ThemePaletteKey(
    val seedColor: Color,
    val isDark: Boolean,
    val style: PaletteStyle,
)

class ThemePaletteCache {
    private class Entry(
        val colorScheme: ColorScheme,
        var refCount: Int,
    )

    private val entries = mutableMapOf<ThemePaletteKey, Entry>()

    fun acquire(key: ThemePaletteKey): ColorScheme {
        val entry = entries.getOrPut(key) {
            Entry(
                colorScheme = dynamicColorScheme(
                    seedColor = key.seedColor,
                    isDark = key.isDark,
                    style = key.style,
                    primary = key.seedColor,
                ),
                refCount = 0,
            )
        }
        entry.refCount++
        return entry.colorScheme
    }

    fun release(key: ThemePaletteKey) {
        val entry = entries[key] ?: return
        entry.refCount--
        if (entry.refCount <= 0) {
            entries.remove(key)
        }
    }
}

val LocalThemePaletteCache = staticCompositionLocalOf<ThemePaletteCache?> { null }

@Composable
fun rememberSharedColorScheme(
    seedColor: Color,
    isDark: Boolean,
    style: PaletteStyle = PaletteStyle.TonalSpot,
): ColorScheme {
    val cache = checkNotNull(LocalThemePaletteCache.current) {
        "LocalThemePaletteCache is not provided. Wrap with AppTheme or provide a ThemePaletteCache."
    }
    val key = ThemePaletteKey(seedColor, isDark, style)
    val colorScheme = remember(cache, key) { cache.acquire(key) }

    DisposableEffect(cache, key) {
        onDispose { cache.release(key) }
    }

    return colorScheme
}
