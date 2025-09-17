package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.stashy.home.*
import org.jetbrains.compose.resources.Font

@Composable
fun AppTypography(): Typography {
    val tomorrowFont = FontFamily(
        Font(Res.font.Tomorrow_Regular, FontWeight.Normal),
        Font(Res.font.Tomorrow_Light, FontWeight.Light),
        Font(Res.font.Tomorrow_Italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.Tomorrow_LightItalic, FontWeight.Light, FontStyle.Italic),
    )

    val playfairFont = FontFamily(
        Font(Res.font.PlayfairDisplay_VariableFont_wght, FontWeight.Normal),
        Font(Res.font.PlayfairDisplay_Italic_VariableFont_wght, FontWeight.Normal, FontStyle.Italic),
    )

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = tomorrowFont),
            displayMedium = displayMedium.copy(fontFamily = tomorrowFont),
            displaySmall = displaySmall.copy(fontFamily = tomorrowFont),
            headlineLarge = headlineLarge.copy(fontFamily = playfairFont),
            headlineMedium = headlineMedium.copy(fontFamily = playfairFont),
            headlineSmall = headlineSmall.copy(fontFamily = playfairFont),
        )
    }
}
