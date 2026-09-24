package data

import Project
import RepoMeta
import RepositoryMeta
import dev.stashy.data.DataSource
import dev.stashy.data.dataSource
import dev.stashy.data.deserialize
import dev.stashy.data.map
import json
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import toProject

class ProjectsRepository(
    val featured: DataSource<List<Project>>,
    val latest: DataSource<List<Project>>,
) {
    constructor() : this(
        featured = dataSource { StaticApi.fetch("featured.json") }
            .deserialize(json),
        latest = dataSource { StaticApi.fetch("repo.json") }
            .deserialize<RepoMeta>(json)
            .map { it.repositories.map(RepositoryMeta::toProject) }
    )

    suspend fun preload() = coroutineScope {
        launch { featured.preload() }
        launch { latest.preload() }
    }
}
