package viewmodel

import Project
import RepoMeta
import RepositoryMeta
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import decodeResource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import toProject

class ProjectsViewmodel() : ViewModel() {
    var state: MutableStateFlow<ProjectScreenState> = MutableStateFlow(Loading)

    suspend fun load() {
        state.emit(Loading)

        runCatching {
            val featured = viewModelScope.async {
                decodeResource<List<Project>>("files/featured.json")
            }

            val latest = viewModelScope.async {
                decodeResource<RepoMeta>("files/repo.json")
                    .repositories.map(RepositoryMeta::toProject)
            }

            ProjectScreenState.Success(
                featured = featured.await(),
                latest = latest.await()
            )
        }.fold(
            onSuccess = { state.emit(it) },
            onFailure = {
                it.printStackTrace()
                state.emit(ProjectScreenState.Failed(it))
            }
        )
    }
}

sealed class ProjectScreenState {
    data object Loading : ProjectScreenState()

    data class Success(
        val featured: List<Project>,
        val latest: List<Project>
    ) : ProjectScreenState()

    data class Failed(val error: Throwable) : ProjectScreenState()
}
