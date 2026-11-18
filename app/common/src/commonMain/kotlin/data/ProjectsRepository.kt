package data

import RepoMeta
import dev.stashy.data.DataSource
import dev.stashy.data.SharedDataSource
import dev.stashy.data.deserialize
import dev.stashy.data.shared
import json

class ProjectsRepository(
    val repos: SharedDataSource<RepoMeta>,
) {
    constructor() : this(
        repos = DataSource { StaticApi.fetch("repo.json") }
            .deserialize<RepoMeta>(json)
            .shared()
    )

    suspend fun preload() {
        repos.preload()
    }
}
