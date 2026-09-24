package dev.stashy.data.source

import dev.stashy.data.DataSource
import kotlin.jvm.JvmInline

@JvmInline
value class SuspendingDataSource<T>(
    private val load: suspend () -> T,
) : DataSource<T> {
    override fun getOrNull(): T? = null

    override suspend fun await(): T = load()
}
