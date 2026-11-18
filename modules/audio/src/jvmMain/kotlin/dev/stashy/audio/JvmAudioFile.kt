package dev.stashy.audio

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader
import java.io.BufferedInputStream
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal actual suspend fun decodeAudio(source: AudioSource, loader: AudioSourceLoader): Result<AudioFile> =
    runCatching {
        val stream = loader.open(source).getOrThrow()
        stream.use { raw ->
            val audio = openDecodedStream(BufferedInputStream(raw.asInputStream(), 1 shl 16))
            audio.use {
                val format = it.format
                val duration = if (it.frameLength > 0 && format.frameRate > 0f) {
                    (it.frameLength / format.frameRate.toDouble()).seconds
                } else {
                    Duration.ZERO
                }
                JvmAudioFile(
                    source = source,
                    loader = loader,
                    format = AudioFormat(format.sampleRate.toInt(), format.channels, format.sampleSizeInBits),
                    duration = duration,
                )
            }
        }
    }

/** Probes the stream header and decodes MP3 via JLayer, everything else via javax.sound. */
internal fun openDecodedStream(input: BufferedInputStream): AudioInputStream {
    input.mark(4)
    val head = ByteArray(4)
    val n = input.read(head, 0, head.size)
    input.reset()
    val isMp3 = n >= 2 && (
            (n >= 3 && head[0] == 'I'.code.toByte() && head[1] == 'D'.code.toByte() && head[2] == '3'.code.toByte()) ||
                    (head[0] == 0xFF.toByte() && (head[1].toInt() and 0xE0) == 0xE0)
            )
    return if (isMp3) {
        MpegAudioFileReader().getAudioInputStream(input)
    } else {
        AudioSystem.getAudioInputStream(input)
    }
}

internal class JvmAudioFile internal constructor(
    private val source: AudioSource,
    private val loader: AudioSourceLoader,
    override val format: AudioFormat,
    override val duration: Duration,
) : AudioFile {
    override suspend fun openStream(): AudioStream = loader.open(source).getOrThrow()

    override fun close() {}
}
