package dev.stashy.audio

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private fun makeWav(sampleRate: Int = 8000, seconds: Double = 0.5): ByteArray {
    val sampleCount = (sampleRate * seconds).toInt()
    val dataSize = sampleCount * 2
    val out = ByteArrayOutputStream()
    fun writeInt(v: Int) {
        out.write(v and 0xFF)
        out.write((v shr 8) and 0xFF)
        out.write((v shr 16) and 0xFF)
        out.write((v shr 24) and 0xFF)
    }

    fun writeShort(v: Int) {
        out.write(v and 0xFF)
        out.write((v shr 8) and 0xFF)
    }

    fun writeStr(s: String) = out.write(s.toByteArray(Charsets.US_ASCII))

    writeStr("RIFF")
    writeInt(36 + dataSize)
    writeStr("WAVE")
    writeStr("fmt ")
    writeInt(16)
    writeShort(1) // PCM
    writeShort(1) // mono
    writeInt(sampleRate)
    writeInt(sampleRate * 2) // byte rate
    writeShort(2) // block align
    writeShort(16) // bits per sample
    writeStr("data")
    writeInt(dataSize)
    for (i in 0 until sampleCount) {
        val v = (kotlin.math.sin(2.0 * kotlin.math.PI * 440.0 * i / sampleRate) * Short.MAX_VALUE * 0.5).toInt()
        writeShort(v)
    }
    return out.toByteArray()
}

class JvmAudioTest {
    @Test
    fun loadsWavFromBytes() = runTest {
        val file = AudioFile.load(AudioSource.Bytes(makeWav(), "test.wav", "audio/wav")).getOrThrow()
        assertEquals(8000, file.format.sampleRate)
        assertEquals(1, file.format.channels)
        assertEquals(16, file.format.bitsPerSample)
        assertEquals(0.5.seconds, file.duration)
        file.close()
    }

    @Test
    fun loadReportsFailureForGarbage() = runTest {
        val result = AudioFile.load(AudioSource.Bytes(byteArrayOf(1, 2, 3), "bad.bin"))
        assertTrue(result.isFailure)
    }

    @Test
    fun streamIsSeekable() = runTest {
        val file = AudioFile.load(AudioSource.Bytes(makeWav(), "test.wav", "audio/wav")).getOrThrow()
        val stream = file.openStream()
        val head = ByteArray(4)
        assertEquals(4, stream.read(head, 0, 4))
        assertEquals("RIFF", head.toString(Charsets.US_ASCII))

        stream.seek(0)
        val headAgain = ByteArray(4)
        assertEquals(4, stream.read(headAgain, 0, 4))
        assertEquals("RIFF", headAgain.toString(Charsets.US_ASCII))
        stream.close()
        file.close()
    }

    @Test
    fun playerReachesTerminalState() = runBlocking {
        val file = AudioFile.load(AudioSource.Bytes(makeWav(seconds = 0.2), "t.wav", "audio/wav")).getOrThrow()
        val player = AudioPlayer()
        player.play(file)
        val final = withTimeoutOrNull(10_000.milliseconds) {
            while (player.state.value == AudioPlayer.State.Loading || player.state.value == AudioPlayer.State.Playing) {
                delay(50.milliseconds)
            }
            player.state.value
        }
        // On machines without an audio device the player reports Failed instead of Playing/Ended.
        assertTrue(
            final != null && final != AudioPlayer.State.Loading && final != AudioPlayer.State.Idle,
            "player stuck in ${player.state.value}",
        )
        player.close()
        file.close()
    }
}
