package theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme

@Composable
fun AppTheme(seedColor: Color, isDark: Boolean, content: @Composable () -> Unit) =
    DynamicMaterialTheme(
        seedColor,
        useDarkTheme = isDark,
        animate = true,
        content = content,
        isExtendedFidelity = true
    )
