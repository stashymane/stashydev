import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import dev.stashy.home.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import org.jetbrains.compose.resources.preloadImageBitmap

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun loadContent(): State<LoadingState> {
    val fonts = listOf(
        preloadFont(Res.font.Tomorrow_Regular),
        preloadFont(Res.font.Tomorrow_Light),
        preloadFont(Res.font.Tomorrow_Italic),
        preloadFont(Res.font.Tomorrow_LightItalic),
        preloadFont(Res.font.PlayfairDisplay_VariableFont_wght),
        preloadFont(Res.font.PlayfairDisplay_Italic_VariableFont_wght),
    )

    val images = listOf(
        preloadImageBitmap(Res.drawable.background)
    )

    val allResources = fonts + images

    return remember {
        derivedStateOf {
            if (allResources.all { it.value != null })
                LoadingState.Complete
            else
                LoadingState.Loading(allResources.count { it.value != null }.toFloat() / fonts.size)
        }
    }
}
