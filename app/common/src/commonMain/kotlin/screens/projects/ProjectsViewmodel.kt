package screens.projects

import Project
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ProjectsRepository
import featuredProjects
import latestProjects
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

        repo.repos.await().fold(
            onSuccess = { meta ->
                state.emit(
                    ProjectScreenState.Success(
                        featured = meta.featuredProjects(),
                        latest = meta.latestProjects(),
                    )
                )
            },
            onFailure = {
                it.printStackTrace()
                state.emit(ProjectScreenState.Failed(it))
            }
        )
    }

    private fun successOrNull(): ProjectScreenState.Success? {
        val meta = repo.repos.getOrNull() ?: return null
        return ProjectScreenState.Success(
            featured = meta.featuredProjects(),
            latest = meta.latestProjects(),
        )
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
