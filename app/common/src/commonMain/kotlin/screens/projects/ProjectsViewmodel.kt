package screens.projects

import Project
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ProjectsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProjectsViewmodel(
    private val repo: ProjectsRepository,
) : ViewModel() {
    var state: MutableStateFlow<ProjectScreenState> = MutableStateFlow(
        successOrNull() ?: ProjectScreenState.Loading
    )

    fun onLaunch() {
        if (state.value is ProjectScreenState.Loading) {
            viewModelScope.launch { load() }
        }
    }

    fun onReload() {
        viewModelScope.launch { load() }
    }

    suspend fun load() {
        state.emit(ProjectScreenState.Loading)

        runCatching {
            val featured = viewModelScope.async { repo.featured.await() }
            val latest = viewModelScope.async { repo.latest.await() }
            ProjectScreenState.Success(
                featured = featured.await(),
                latest = latest.await(),
            )
        }.fold(
            onSuccess = { state.emit(it) },
            onFailure = {
                it.printStackTrace()
                state.emit(ProjectScreenState.Failed(it))
            }
        )
    }

    private fun successOrNull(): ProjectScreenState.Success? {
        val featured = repo.featured.getOrNull() ?: return null
        val latest = repo.latest.getOrNull() ?: return null
        return ProjectScreenState.Success(featured, latest)
    }
}

sealed class ProjectScreenState {
    object Loading : ProjectScreenState()

    class Success(
        val featured: List<Project>,
        val latest: List<Project>
    ) : ProjectScreenState()

    class Failed(val error: Throwable) : ProjectScreenState()
}
