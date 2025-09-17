import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
expect fun loadContent(): State<LoadingState>

sealed class LoadingState {
    data class Loading(val progress: Float) : LoadingState()
    data object Complete : LoadingState()
}
