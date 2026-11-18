package dev.stashy.audio.loader.ktor

import dev.stashy.audio.AudioSource
import dev.stashy.audio.AudioSourceLoader
import dev.stashy.audio.AudioStream
import dev.stashy.audio.ByteArrayAudioStream
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*

/**
 * [AudioSourceLoader] backed by a Ktor [HttpClient]. HTTP sources are streamed through range
 * requests (one chunk in memory at a time), so seeking is cheap and large files stay bounded.
 */
public class KtorAudioLoader(
    private val client: HttpClient = HttpClient(),
) : AudioSourceLoader {
    override suspend fun open(source: AudioSource): Result<AudioStream> = runCatching {
        when (source) {
            is AudioSource.Bytes -> ByteArrayAudioStream(source.data)
            is AudioSource.Url -> KtorRangeAudioStream(client, source.url, source.headers)
        }
    }
}

private class KtorRangeAudioStream(
    private val client: HttpClient,
    private val url: String,
    private val extraHeaders: Map<String, String>,
    private val chunkSize: Long = 512 * 1024,
) : AudioStream {
    override var size: Long = -1L
        private set

    override var position: Long = 0L
        private set

    private var chunk: ByteArray? = null
    private var chunkStart: Long = -1L

    override suspend fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (size in 0..position) return -1
        if (!ensureChunk()) return -1
        val c = chunk!!
        val chunkOffset = (position - chunkStart).toInt()
        var n = minOf(length.toLong(), (c.size - chunkOffset).toLong())
        if (size >= 0) n = minOf(n, size - position)
        if (n <= 0) return -1
        c.copyInto(buffer, offset, chunkOffset, chunkOffset + n.toInt())
        position += n
        return n.toInt()
    }

    private suspend fun ensureChunk(): Boolean {
        val c = chunk
        if (c != null && position in chunkStart until chunkStart + c.size) return true
        val response = client.get(url) {
            extraHeaders.forEach { (key, value) -> header(key, value) }
            header(HttpHeaders.Range, "bytes=$position-${position + chunkSize - 1}")
        }
        val data = readBounded(response)
        val contentRange = response.headers[HttpHeaders.ContentRange]
        if (size < 0) size = totalSize(response, contentRange)
        if (data.isEmpty()) {
            chunk = null
            return false
        }
        if (response.status != HttpStatusCode.PartialContent && position >= data.size) return false
        chunk = data
        chunkStart = if (response.status == HttpStatusCode.PartialContent) {
            contentRange?.substringAfter("bytes ")?.substringBefore('-')?.toLongOrNull() ?: position
        } else {
            0L
        }
        return true
    }

    /** Reads at most one chunk worth of bytes, even when the server ignored the range request. */
    private suspend fun readBounded(response: HttpResponse): ByteArray {
        val limit = chunkSize.toInt()
        val data = ByteArray(limit)
        var read = 0
        val channel = response.bodyAsChannel()
        while (read < limit) {
            val n = channel.readAvailable(data, read, limit - read)
            if (n == -1) break
            read += n
        }
        channel.cancel(null)
        return if (read == limit) data else data.copyOf(read)
    }

    private fun totalSize(response: HttpResponse, contentRange: String?): Long =
        contentRange
            ?.substringAfterLast('/')
            ?.takeIf { it != "*" }
            ?.toLongOrNull()
            ?: response.headers[HttpHeaders.ContentLength]?.toLongOrNull()
            ?: -1L

    override suspend fun seek(position: Long) {
        this.position = if (size >= 0) position.coerceIn(0, size) else position.coerceAtLeast(0)
        chunk = null
    }

    override fun close() {
        chunk = null
    }
}
