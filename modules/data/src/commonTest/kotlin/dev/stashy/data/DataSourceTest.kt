package dev.stashy.data

import dev.stashy.data.source.map
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.*

class DataSourceTest {
    @Test
    fun dataSourceDoesNotCacheByDefault() = runTest {
        var loads = 0
        val source = dataSource {
            loads++
            "value"
        }

        assertEquals("value", source.await())
        assertEquals("value", source.await())
        assertEquals(2, loads)
        assertNull(source.getOrNull())
    }

    @Test
    fun cachedStoresSuccessfulResult() = runTest {
        var loads = 0
        val source = dataSource {
            loads++
            "value"
        }.cached()

        assertEquals("value", source.await())
        assertEquals("value", source.await())
        assertEquals("value", source.getOrNull())
        assertEquals(1, loads)
    }

    @Test
    fun mapDoesNotCacheIntermediateResult() = runTest {
        var loads = 0
        var maps = 0
        val source = dataSource {
            loads++
            1
        }.map {
            maps++
            it * 2
        }

        assertEquals(2, source.await())
        assertEquals(2, source.await())
        assertEquals(2, loads)
        assertEquals(2, maps)
        assertNull(source.getOrNull())
    }

    @Test
    fun cachedCachesFinalMappedResultOnly() = runTest {
        var loads = 0
        var maps = 0
        val source = dataSource {
            loads++
            1
        }.map {
            maps++
            it * 2
        }.cached()

        assertEquals(2, source.await())
        assertEquals(2, source.await())
        assertEquals(2, source.getOrNull())
        assertEquals(1, loads)
        assertEquals(1, maps)
    }

    @Test
    fun failureDoesNotCacheAndClearsInFlight() = runTest {
        var loads = 0
        val source = dataSource {
            loads++
            error("boom")
        }.cached()

        val first = assertFailsWith<IllegalStateException> { source.await() }
        assertEquals("boom", first.message)
        assertNull(source.getOrNull())
        assertEquals(1, loads)

        val second = assertFailsWith<IllegalStateException> { source.await() }
        assertEquals("boom", second.message)
        assertNull(source.getOrNull())
        assertEquals(2, loads)
    }

    @Test
    fun awaitAfterFailureRetriesAndCachesSuccess() = runTest {
        var loads = 0
        val source = dataSource {
            loads++
            if (loads == 1) error("temporary")
            "ok"
        }.cached()

        assertFailsWith<IllegalStateException> { source.await() }
        assertNull(source.getOrNull())
        assertEquals(1, loads)

        assertEquals("ok", source.await())
        assertEquals("ok", source.getOrNull())
        assertEquals(2, loads)

        assertEquals("ok", source.await())
        assertEquals(2, loads)
    }

    @Test
    fun mapFailureDoesNotCacheAndRetries() = runTest {
        var loads = 0
        var maps = 0
        val source = dataSource {
            loads++
            "payload"
        }.map {
            maps++
            if (maps == 1) error("parse failed")
            it.uppercase()
        }.cached()

        assertFailsWith<IllegalStateException> { source.await() }
        assertNull(source.getOrNull())
        assertEquals(1, loads)
        assertEquals(1, maps)

        assertEquals("PAYLOAD", source.await())
        assertEquals("PAYLOAD", source.getOrNull())
        assertEquals(2, loads)
        assertEquals(2, maps)
    }

    @Test
    fun concurrentAwaitCoalescesInFlightLoad() = runTest {
        var loads = 0
        val started = CompletableDeferred<Unit>()
        val gate = CompletableDeferred<Unit>()
        val source = dataSource {
            loads++
            started.complete(Unit)
            gate.await()
            "shared"
        }.cached()

        coroutineScope {
            val first = async { source.await() }
            val second = async { source.await() }
            started.await()
            yield()
            gate.complete(Unit)
            assertEquals("shared", first.await())
            assertEquals("shared", second.await())
        }

        assertEquals(1, loads)
        assertEquals("shared", source.getOrNull())
    }

    @Test
    fun preloadWarmsCachedSource() = runTest {
        var loads = 0
        val source = dataSource {
            loads++
            "value"
        }.cached()

        assertNull(source.getOrNull())
        source.preload()
        assertEquals("value", source.getOrNull())
        assertEquals(1, loads)

        source.preload()
        assertEquals(1, loads)
    }

    @Test
    fun concurrentAwaitFailurePropagatesAndAllowsRetry() = runTest {
        var loads = 0
        val started = CompletableDeferred<Unit>()
        val gate = CompletableDeferred<Unit>()
        val source = dataSource {
            loads++
            started.complete(Unit)
            gate.await()
            if (loads == 1) error("shared failure")
            "recovered"
        }.cached()

        coroutineScope {
            val first = async { runCatching { source.await() } }
            val second = async { runCatching { source.await() } }
            started.await()
            yield()
            gate.complete(Unit)

            val firstResult = first.await()
            val secondResult = second.await()
            assertTrue(firstResult.isFailure)
            assertTrue(secondResult.isFailure)
            assertEquals("shared failure", firstResult.exceptionOrNull()?.message)
            assertEquals("shared failure", secondResult.exceptionOrNull()?.message)
        }

        assertNull(source.getOrNull())
        assertEquals(1, loads)

        assertEquals("recovered", source.await())
        assertEquals(2, loads)
        assertEquals("recovered", source.getOrNull())
    }
}
