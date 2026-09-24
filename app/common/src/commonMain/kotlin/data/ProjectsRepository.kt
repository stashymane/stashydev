package data

import Project
import RepoMeta
import RepositoryMeta
import dev.stashy.data.DataSource
import dev.stashy.data.dataSource
import dev.stashy.data.deserialize
import dev.stashy.data.map
import dev.stashy.home.Res
import json
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import toProject

class ProjectsRepository(
    val featured: DataSource<List<Project>>,
    val latest: DataSource<List<Project>>,
) {
    constructor() : this(
        featured = dataSource { Res.readBytes("files/featured.json").decodeToString() }
            .deserialize(json),
        latest = dataSource { Res.readBytes("files/repo.json").decodeToString() }
            .deserialize<RepoMeta>(json)
            .map { it.repositories.map(RepositoryMeta::toProject) }
    )

    suspend fun preload() = coroutineScope {
        launch { featured.preload() }
        launch { latest.preload() }
    }
}
