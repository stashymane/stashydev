package data

import UserMeta
import dev.stashy.data.DataSource
import dev.stashy.data.dataSource
import dev.stashy.data.deserialize
import dev.stashy.home.Res
import json

class AboutRepository(
    val data: DataSource<UserMeta>,
) {
    constructor() : this(
        data = dataSource { Res.readBytes("files/user.json").decodeToString() }
            .deserialize(json)
    )

    suspend fun preload() {
        data.preload()
    }
}
