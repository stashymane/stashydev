package data

import UserMeta
import dev.stashy.data.DataSource
import dev.stashy.data.SharedDataSource
import dev.stashy.data.deserialize
import dev.stashy.data.shared
import json

class AboutRepository(
    val data: SharedDataSource<UserMeta>,
) {
    constructor() : this(
        data = DataSource { StaticApi.fetch("user.json") }
            .deserialize<UserMeta>(json)
            .shared()
    )

    suspend fun preload() {
        data.preload()
    }
}
