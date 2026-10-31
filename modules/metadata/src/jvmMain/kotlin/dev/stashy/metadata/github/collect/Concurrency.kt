package dev.stashy.metadata.github.collect

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

/**
 * Runs [transform] over the elements with at most [concurrency] calls in flight.
 * Results keep the input order; a failing call cancels the rest and rethrows.
 */
internal suspend fun <T, R> Iterable<T>.mapConcurrent(
    concurrency: Int,
    transform: suspend (T) -> R,
): List<R> = coroutineScope {
    val permits = Semaphore(concurrency)
    map { item -> async { permits.withPermit { transform(item) } } }.awaitAll()
}
