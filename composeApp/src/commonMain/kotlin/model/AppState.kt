package model

import Project
import dev.stashy.home.Res
import json
import kotlinx.coroutines.flow.MutableStateFlow

class AppState(
    projects: List<Project>? = null
) {
    val projectState: MutableStateFlow<ProjectState> = MutableStateFlow(
        if (projects != null)
            ProjectState.Loaded(projects)
        else ProjectState.Loading
    )

    suspend fun loadProjects() {
        projectState.emit(ProjectState.Loading)

        runCatching {
            val content = Res.readBytes("files/projects.json").decodeToString()
            ProjectState.Loaded(json.decodeFromString(content))
        }.fold(
            onSuccess = {
                projectState.emit(it)
            },
            onFailure = { err ->
                projectState.emit(ProjectState.Failed(err))
            }
        )
    }
}

sealed class ProjectState {
    object Loading : ProjectState()
    class Loaded(val projects: List<Project>) : ProjectState()
    class Failed(val error: Throwable) : ProjectState()
}
