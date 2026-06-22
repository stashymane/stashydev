package model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val themeMode: ThemeMode = ThemeMode.Auto,
    val bottomNav: Boolean = false
)

@Serializable
enum class ThemeMode {
    Auto,
    Dark,
    Light
}

@Composable
fun ThemeMode.isDark(): Boolean = when (this) {
    ThemeMode.Dark -> true
    ThemeMode.Light -> false
    ThemeMode.Auto -> isSystemInDarkTheme()
}
