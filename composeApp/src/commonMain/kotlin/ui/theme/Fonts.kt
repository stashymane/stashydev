package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import dev.stashy.home.Inter_Italic_VariableFont_opsz_wght
import dev.stashy.home.Inter_VariableFont_opsz_wght
import dev.stashy.home.PlayfairDisplay_Italic_VariableFont_wght
import dev.stashy.home.PlayfairDisplay_VariableFont_wght
import dev.stashy.home.Res
import dev.stashy.home.SpaceGrotesk_VariableFont_wght
import org.jetbrains.compose.resources.Font

@Composable
fun appTypography(): Typography {
    val interFont = FontFamily(
        Font(
            Res.font.Inter_VariableFont_opsz_wght,
            Normal,
            Normal
        ),
        Font(
            Res.font.Inter_Italic_VariableFont_opsz_wght,
            Normal,
            Italic
        )
    )

    val playfairFont = FontFamily(
        Font(
            Res.font.PlayfairDisplay_VariableFont_wght,
            Normal,
            Normal
        ),
        Font(
            Res.font.PlayfairDisplay_Italic_VariableFont_wght,
            Normal,
            Italic
        )
    )

    val spaceGroteskFont = FontFamily(
        Font(
            Res.font.SpaceGrotesk_VariableFont_wght,
            Normal,
            Normal
        )
    )

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = spaceGroteskFont),
            displayMedium = displayMedium.copy(fontFamily = spaceGroteskFont),
            displaySmall = displaySmall.copy(fontFamily = spaceGroteskFont),
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
