package dev.stashy.data.source

import dev.stashy.data.DataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class CombinedDataSource<A, B, R>(
    private val first: DataSource<A>,
    private val second: DataSource<B>,
    private val transform: (A, B) -> R,
) : DataSource<R> {
    override fun getOrNull(): R? {
        val a = first.getOrNull() ?: return null
        val b = second.getOrNull() ?: return null
        return transform(a, b)
    }

    override suspend fun await(): R = coroutineScope {
        val a = async { first.await() }
        val b = async { second.await() }
        transform(a.await(), b.await())
    }

    override suspend fun preload() {
        coroutineScope {
            launch { first.preload() }
            launch { second.preload() }
        }
    }
}
