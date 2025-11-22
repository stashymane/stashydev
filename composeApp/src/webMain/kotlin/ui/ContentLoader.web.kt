package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.Font
import dev.stashy.home.PlayfairDisplay_Italic_VariableFont_wght
import dev.stashy.home.PlayfairDisplay_VariableFont_wght
import dev.stashy.home.Res
import dev.stashy.home.Tomorrow_Italic
import dev.stashy.home.Tomorrow_Light
import dev.stashy.home.Tomorrow_LightItalic
import dev.stashy.home.Tomorrow_Regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun loadContent(): State<LoadingState> {
    val fonts: List<State<Font?>> = listOf(
        preloadFont(Res.font.Tomorrow_Regular),
        preloadFont(Res.font.Tomorrow_Light),
        preloadFont(Res.font.Tomorrow_Italic),
        preloadFont(Res.font.Tomorrow_LightItalic),
        preloadFont(Res.font.PlayfairDisplay_VariableFont_wght),
        preloadFont(Res.font.PlayfairDisplay_Italic_VariableFont_wght),
    )

    val images: List<State<ImageBitmap?>> = listOf(

    )

    val allResources = fonts + images

    return remember {
        derivedStateOf {
            if (allResources.all { it.value != null })
                LoadingState.Complete
            else
                LoadingState.Loading(allResources.count { it.value != null }
                    .toFloat() / allResources.size)
        }
    }
}
