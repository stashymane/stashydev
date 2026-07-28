package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.stashy.home.Inter_Italic_VariableFont_opsz_wght
import dev.stashy.home.Inter_VariableFont_opsz_wght
import dev.stashy.home.PlayfairDisplay_Italic_VariableFont_wght
import dev.stashy.home.PlayfairDisplay_VariableFont_wght
import dev.stashy.home.Res
import org.jetbrains.compose.resources.Font

private val fontWeights = listOf(
    FontWeight.W100,
    FontWeight.W200,
    FontWeight.W300,
    FontWeight.W400,
    FontWeight.W500,
    FontWeight.W600,
    FontWeight.W700,
    FontWeight.W800,
    FontWeight.W900
)

private inline fun variableFontFamily(fn: (FontWeight) -> Font): List<Font> {
    return fontWeights.map(fn)
}

@Composable
fun appTypography(): Typography {
    val interFont = FontFamily(
        variableFontFamily { Font(Res.font.Inter_VariableFont_opsz_wght, it) } +
                variableFontFamily {
                    Font(
                        Res.font.Inter_Italic_VariableFont_opsz_wght,
                        it,
                        FontStyle.Italic
                    )
                }
    )

    val playfairFont = FontFamily(
        variableFontFamily { Font(Res.font.PlayfairDisplay_VariableFont_wght, it) } +
                variableFontFamily {
                    Font(
                        Res.font.PlayfairDisplay_Italic_VariableFont_wght,
                        it,
                        FontStyle.Italic
                    )
                }
    )

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = interFont),
            displayMedium = displayMedium.copy(fontFamily = interFont),
            displaySmall = displaySmall.copy(fontFamily = interFont),
            headlineLarge = headlineLarge.copy(fontFamily = playfairFont),
            headlineMedium = headlineMedium.copy(fontFamily = playfairFont),
            headlineSmall = headlineSmall.copy(fontFamily = playfairFont),
            titleLarge = bodyLarge.copy(fontFamily = interFont),
            titleMedium = bodyLarge.copy(fontFamily = interFont),
            titleSmall = bodyLarge.copy(fontFamily = interFont),
            bodyLarge = bodyLarge.copy(fontFamily = interFont),
            bodyMedium = bodyLarge.copy(fontFamily = interFont),
            bodySmall = bodyLarge.copy(fontFamily = interFont),
            labelLarge = labelLarge.copy(fontFamily = interFont),
            labelMedium = labelMedium.copy(fontFamily = interFont),
            labelSmall = labelSmall.copy(fontFamily = interFont),
        )
    }
}
