package vm

import Project
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stashy.home.Res
import json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class HomeScreenViewmodel : ViewModel() {
    val stateFlow = MutableStateFlow<State>(State.Loading)

    fun load() = viewModelScope.launch {
        try {
            stateFlow.emit(State.Loading)
            val projects = decodeProjects()
            stateFlow.emit(State.Loaded(projects))
        } catch (e: Exception) {
            e.printStackTrace()
            stateFlow.emit(State.Error(e))
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun decodeProjects(): List<Project> =
        json.decodeFromString<List<Project>>(Res.readBytes("files/projects.json").decodeToString())

    sealed class State() {
        data object Loading : State()
        data class Loaded(val projects: List<Project>) : State()
        data class Error(val exception: Throwable?) : State()
    }
}
