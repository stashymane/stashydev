package screens.about

import UserMeta
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.AboutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AboutViewmodel(
    private val repo: AboutRepository,
) : ViewModel() {
    var state: MutableStateFlow<AboutScreenState> = MutableStateFlow(
        successOrNull() ?: AboutScreenState.Loading
    )

    fun onLaunch() {
        if (state.value is AboutScreenState.Loading) {
            viewModelScope.launch { load() }
        }
    }

    fun onReload() {
        viewModelScope.launch { load() }
    }

    suspend fun load() {
        state.emit(AboutScreenState.Loading)

        runCatching {
            AboutScreenState.Success(repo.data.await())
        }.fold(
            onSuccess = { state.emit(it) },
            onFailure = {
                it.printStackTrace()
                state.emit(AboutScreenState.Failed(it))
            }
        )
    }

    private fun successOrNull(): AboutScreenState.Success? =
        repo.data.getOrNull()?.let { AboutScreenState.Success(it) }
}

sealed class AboutScreenState {
    object Loading : AboutScreenState()
    class Success(val meta: UserMeta) : AboutScreenState()
    class Failed(val error: Throwable) : AboutScreenState()
}
