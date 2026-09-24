package data

import Project
import RepoMeta
import RepositoryMeta
import dev.stashy.data.CachedDataSource
import dev.stashy.data.cached
import dev.stashy.data.dataSource
import dev.stashy.data.deserialize
import dev.stashy.data.source.map
import json
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import toProject

class ProjectsRepository(
    val featured: CachedDataSource<List<Project>>,
    val latest: CachedDataSource<List<Project>>,
) {
    constructor() : this(
        featured = dataSource { StaticApi.fetch("featured.json") }
            .deserialize<List<Project>>(json)
            .cached(),
        latest = dataSource { StaticApi.fetch("repo.json") }
            .deserialize<RepoMeta>(json)
            .map { it.repositories.map(RepositoryMeta::toProject) }
            .cached()
    )

    suspend fun preload() = coroutineScope {
        launch { featured.preload() }
        launch { latest.preload() }
    }
}
