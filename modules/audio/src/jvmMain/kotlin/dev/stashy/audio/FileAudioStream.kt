package dev.stashy.audio

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.RandomAccessFile

internal class FileAudioStream(file: File) : AudioStream {
    private val raf = RandomAccessFile(file, "r")

    override val size: Long
        get() = raf.length()

    override var position: Long = 0L
        private set

    override suspend fun read(buffer: ByteArray, offset: Int, length: Int): Int = withContext(Dispatchers.IO) {
        raf.seek(this@FileAudioStream.position)
        val n = raf.read(buffer, offset, length)
        if (n > 0) this@FileAudioStream.position += n
        n
    }

    override suspend fun seek(position: Long) = withContext(Dispatchers.IO) {
        this@FileAudioStream.position = position.coerceIn(0, size)
        raf.seek(this@FileAudioStream.position)
    }

    override fun close() = raf.close()
}
