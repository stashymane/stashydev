import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import kotlinx.coroutines.delay

@Composable
actual fun loadContent(): State<LoadingState> = derivedStateOf { LoadingState.Complete }

@Composable
private fun fakeLoadContent(): State<LoadingState> = produceState<LoadingState>(LoadingState.Loading(0f)) {
    var progress = 0f
    while (progress < 1f) {
        delay(50)
        progress += 0.1f
        value = LoadingState.Loading((progress + 0.1f).coerceIn(0f..1f))
    }
    value = LoadingState.Complete
}
