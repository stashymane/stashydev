package dev.stashy.data.source

import dev.stashy.data.DataSource
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CompletableDeferred

internal class CachingDataSource<T>(
    private val load: suspend () -> T,
) : DataSource<T> {
    private val cached = atomic<T?>(null)
    private val inFlight = atomic<CompletableDeferred<T>?>(null)

    override fun getOrNull(): T? = cached.value

    override suspend fun await(): T {
        while (true) {
            cached.value?.let { return it }

            val existing = inFlight.value
            if (existing != null) {
                return existing.await()
            }

            val created = CompletableDeferred<T>()
            if (!inFlight.compareAndSet(null, created)) {
                continue
            }

            return try {
                val result = load()
                cached.value = result
                inFlight.compareAndSet(created, null)
                created.complete(result)
                result
            } catch (error: Throwable) {
                inFlight.compareAndSet(created, null)
                created.completeExceptionally(error)
                throw error
            }
        }
    }
}
