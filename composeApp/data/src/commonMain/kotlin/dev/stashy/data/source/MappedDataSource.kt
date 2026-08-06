package dev.stashy.data.source

import dev.stashy.data.DataSource

internal class MappedDataSource<T, R>(
    private val source: DataSource<T>,
    private val transform: (T) -> R,
) : DataSource<R> {
    override fun getOrNull(): R? = source.getOrNull()?.let(transform)

    override suspend fun await(): R = transform(source.await())

    override suspend fun preload() = source.preload()
}
