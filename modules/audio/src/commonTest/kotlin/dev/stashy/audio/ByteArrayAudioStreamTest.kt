package dev.stashy.audio

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ByteArrayAudioStreamTest {
    @Test
    fun readSeekAndEof() = runTest {
        val stream = ByteArrayAudioStream(byteArrayOf(1, 2, 3, 4, 5))
        assertEquals(5, stream.size)
        assertEquals(0, stream.position)

        val buf = ByteArray(2)
        assertEquals(2, stream.read(buf, 0, 2))
        assertEquals(listOf<Byte>(1, 2), buf.toList())
        assertEquals(2, stream.position)

        stream.seek(4)
        assertEquals(1, stream.read(buf, 0, 2))
        assertEquals(5, buf[0])

        stream.seek(0)
        assertEquals(5, stream.read(ByteArray(10), 0, 10))
        assertEquals(-1, stream.read(buf, 0, 2))
    }

    @Test
    fun seekIsClamped() = runTest {
        val stream = ByteArrayAudioStream(byteArrayOf(1, 2, 3))
        stream.seek(100)
        assertEquals(3, stream.position)
        stream.seek(-5)
        assertEquals(0, stream.position)
    }
}
