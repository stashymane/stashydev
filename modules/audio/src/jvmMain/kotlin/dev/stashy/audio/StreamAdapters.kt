package dev.stashy.audio

import kotlinx.coroutines.runBlocking
import java.io.InputStream

/** Bridges the suspending [AudioStream] API to the synchronous java.io API used by javax.sound. */
internal fun AudioStream.asInputStream(): InputStream = object : InputStream() {
    override fun read(): Int {
        val b = ByteArray(1)
        val n = runBlocking { this@asInputStream.read(b, 0, 1) }
        return if (n == -1) -1 else b[0].toInt() and 0xFF
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int =
        runBlocking { this@asInputStream.read(b, off, len) }

    override fun close() {
        runBlocking { this@asInputStream.close() }
    }
}
