package components.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable

@Composable
fun <T> NullSwapper(current: T?, content: @Composable (T) -> Unit) {
    AnimatedContent(current) {
        when (it) {
            null -> LinearProgressIndicator()
            else -> content(it)
        }
    }
}