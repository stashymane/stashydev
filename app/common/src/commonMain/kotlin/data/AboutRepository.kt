package data

import UserMeta
import dev.stashy.data.CachedDataSource
import dev.stashy.data.cached
import dev.stashy.data.dataSource
import dev.stashy.data.deserialize
import json

class AboutRepository(
    val data: CachedDataSource<UserMeta>,
) {
    constructor() : this(
        data = dataSource { StaticApi.fetch("user.json") }
            .deserialize<UserMeta>(json)
            .cached()
    )

    suspend fun preload() {
        data.preload()
    }
}
