package data

import RepoMeta
import dev.stashy.data.CachedDataSource
import dev.stashy.data.cached
import dev.stashy.data.dataSource
import dev.stashy.data.deserialize
import json

class ProjectsRepository(
    val repos: CachedDataSource<RepoMeta>,
) {
    constructor() : this(
        repos = dataSource { StaticApi.fetch("repo.json") }
            .deserialize<RepoMeta>(json)
            .cached()
    )

    suspend fun preload() {
        repos.preload()
    }
}
