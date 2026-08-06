package dev.stashy.data

import dev.stashy.data.source.CachingDataSource
import dev.stashy.data.source.CombinedDataSource
import dev.stashy.data.source.MappedDataSource

/**
 * Lazy, cacheable data source. After a successful [await] or [preload],
 * [getOrNull] returns the value immediately.
 */
interface DataSource<out T> {
    /** Cached value if already loaded, otherwise null. */
    fun getOrNull(): T?

    /** Return the cache or start loading the content. */
    suspend fun await(): T

    /** Ensure the value is cached. Returns immediately if the content is loaded already. */
    suspend fun preload() {
        await()
    }
}

fun <T> dataSource(load: suspend () -> T): DataSource<T> = CachingDataSource(load)

fun <T, R> DataSource<T>.map(transform: (T) -> R): DataSource<R> =
    MappedDataSource(this, transform)

fun <A, B, R> combine(
    first: DataSource<A>,
    second: DataSource<B>,
    transform: (A, B) -> R,
): DataSource<R> = CombinedDataSource(first, second, transform)

inline fun <T, R> DataSource<T>.getOrElse(transform: (T) -> R, orElse: () -> R) =
    getOrNull()?.let(transform) ?: orElse()
