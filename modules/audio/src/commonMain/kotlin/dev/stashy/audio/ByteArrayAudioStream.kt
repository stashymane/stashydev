package dev.stashy.audio

public class ByteArrayAudioStream(private val data: ByteArray) : AudioStream {
    override val size: Long
        get() = data.size.toLong()

    override var position: Long = 0L
        private set

    override suspend fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (position >= size) return -1
        val n = minOf(length.toLong(), size - position).toInt()
        data.copyInto(buffer, offset, position.toInt(), position.toInt() + n)
        position += n
        return n
    }

    override suspend fun seek(position: Long) {
        this.position = position.coerceIn(0, size)
    }

    override fun close() {}
}
