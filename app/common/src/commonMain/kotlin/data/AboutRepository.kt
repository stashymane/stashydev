package data

import UserMeta
import dev.stashy.data.DataSource
import dev.stashy.data.dataSource
import dev.stashy.data.deserialize
import json

class AboutRepository(
    val data: DataSource<UserMeta>,
) {
    constructor() : this(
        data = dataSource { StaticApi.fetch("user.json") }
            .deserialize(json)
    )

    suspend fun preload() {
        data.preload()
    }
}
