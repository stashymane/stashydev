package dev.stashy.audio

internal data class HttpRangeResponse(
    val data: ByteArray,
    val dataStart: Long,
    val totalSize: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as HttpRangeResponse

        if (dataStart != other.dataStart) return false
        if (totalSize != other.totalSize) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dataStart.hashCode()
        result = 31 * result + totalSize.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}

/** Fetches the byte range [start]..[end] of [url]. [dataStart] may be 0 when the server ignored the range. */
internal expect suspend fun httpRangeRequest(
    url: String,
    headers: Map<String, String>,
    start: Long,
    end: Long,
): HttpRangeResponse

/**
 * Seekable stream over HTTP, backed by range requests. Only a single chunk is held in memory,
 * so arbitrarily large files can be streamed; [seek] simply drops the current chunk and the
 * next [read] issues a new range request.
 */
internal class RangeAudioStream(
    private val url: String,
    private val headers: Map<String, String>,
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
        val response = httpRangeRequest(url, headers, position, position + chunkSize - 1)
        if (size < 0) size = response.totalSize
        if (response.data.isEmpty()) {
            chunk = null
            return false
        }
        if (response.dataStart == 0L && position >= response.data.size) return false
        chunk = response.data
        chunkStart = response.dataStart
        return true
    }

    override suspend fun seek(position: Long) {
        this.position = if (size >= 0) position.coerceIn(0, size) else position.coerceAtLeast(0)
        chunk = null
    }

    override fun close() {
        chunk = null
    }
}
