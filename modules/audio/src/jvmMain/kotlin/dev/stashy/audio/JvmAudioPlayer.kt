package dev.stashy.audio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import javax.sound.sampled.AudioFormat as JAudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.FloatControl
import javax.sound.sampled.SourceDataLine
import kotlin.math.log10
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public actual fun AudioPlayer(): AudioPlayer = JvmAudioPlayer()

@OptIn(ExperimentalCoroutinesApi::class)
internal class JvmAudioPlayer : AudioPlayer {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO.limitedParallelism(1))

    private val _state = MutableStateFlow(AudioPlayer.State.Idle)
    private val _position = MutableStateFlow(Duration.ZERO)
    override val state: StateFlow<AudioPlayer.State> = _state
    override val position: StateFlow<Duration> = _position

    override var volume: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            line?.let { applyVolume(it, field) }
        }

    private var line: SourceDataLine? = null
    private var pumpJob: Job? = null
    private var currentFile: JvmAudioFile? = null
    private var pcmStream: AudioInputStream? = null
    private var framesFed: Long = 0L

    override suspend fun play(track: AudioFile) {
        stopInternal()
        if (track !is JvmAudioFile) {
            _state.value = AudioPlayer.State.Failed
            return
        }
        _state.value = AudioPlayer.State.Loading
        try {
            currentFile = track
            val decoded = openTrackStream(track)
            val targetFormat = pcm16(decoded.format).takeIf {
                it != decoded.format && AudioSystem.isConversionSupported(it, decoded.format)
            } ?: decoded.format
            val pcm = if (targetFormat != decoded.format) {
                AudioSystem.getAudioInputStream(targetFormat, decoded)
            } else {
                decoded
            }
            pcmStream = pcm

            val info = DataLine.Info(SourceDataLine::class.java, targetFormat)
            check(AudioSystem.isLineSupported(info)) { "No source data line available for $targetFormat" }
            val newLine = AudioSystem.getSourceDataLine(targetFormat)
            newLine.open(targetFormat)
            applyVolume(newLine, volume)
            line = newLine
            framesFed = 0
            _position.value = Duration.ZERO
            newLine.start()
            pumpJob = scope.launch { pump(pcm, newLine) }
            _state.value = AudioPlayer.State.Playing
        } catch (t: Throwable) {
            _state.value = AudioPlayer.State.Failed
        }
    }

    private suspend fun openTrackStream(file: JvmAudioFile): AudioInputStream {
        val stream = file.openStream()
        return openDecodedStream(BufferedInputStream(stream.asInputStream(), 1 shl 16))
    }

    private suspend fun CoroutineScope.pump(pcm: AudioInputStream, line: SourceDataLine) {
        val buffer = ByteArray(16 * 1024)
        val frameSize = pcm.format.frameSize.coerceAtLeast(1)
        val frameRate = pcm.format.frameRate.toDouble()
        try {
            while (isActive) {
                val n = withContext(Dispatchers.IO) { pcm.read(buffer, 0, buffer.size) }
                if (n == -1) break
                withContext(Dispatchers.IO) { line.write(buffer, 0, n) }
                framesFed += n / frameSize
                _position.value = (framesFed / frameRate).seconds
            }
            withContext(Dispatchers.IO) { line.drain() }
            if (isActive) _state.value = AudioPlayer.State.Ended
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (t: Throwable) {
            if (isActive) _state.value = AudioPlayer.State.Failed
        }
    }

    override fun pause() {
        line?.stop()
        if (_state.value == AudioPlayer.State.Playing) _state.value = AudioPlayer.State.Paused
    }

    override fun resume() {
        line?.start()
        if (_state.value == AudioPlayer.State.Paused) _state.value = AudioPlayer.State.Playing
    }

    override fun stop() {
        stopInternal()
        _state.value = AudioPlayer.State.Stopped
        _position.value = Duration.ZERO
    }

    private fun stopInternal() {
        pumpJob?.cancel()
        pumpJob = null
        line?.apply { stop(); flush(); close() }
        line = null
        runCatching { pcmStream?.close() }
        pcmStream = null
        currentFile = null
        framesFed = 0
    }

    override suspend fun seek(position: Duration) {
        val pcm = pcmStream ?: return
        val frameSize = pcm.format.frameSize.coerceAtLeast(1)
        val frameRate = pcm.format.frameRate.toDouble()
        if (frameRate <= 0) return
        val targetFrame = (position.inWholeMicroseconds / 1_000_000.0 * frameRate).toLong().coerceAtLeast(0)
        val targetByte = targetFrame * frameSize
        withContext(Dispatchers.IO) {
            line?.flush()
            val delta = targetByte - framesFed * frameSize
            if (delta >= 0) {
                runCatching { pcm.skip(delta) }
            } else {
                val file = currentFile ?: return@withContext
                runCatching { pcm.close() }
                val decoded = openTrackStream(file)
                pcmStream = runCatching { AudioSystem.getAudioInputStream(pcm.format, decoded) }.getOrElse { decoded }
                runCatching { pcmStream!!.skip(targetByte) }
            }
            framesFed = targetFrame
        }
        _position.value = position
    }

    override fun close() {
        stopInternal()
        scope.cancel()
    }

    private fun pcm16(source: JAudioFormat): JAudioFormat = JAudioFormat(
        source.sampleRate,
        16,
        source.channels,
        true,
        false,
    )

    private fun applyVolume(line: SourceDataLine, volume: Float) {
        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            val control = line.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
            control.value = if (volume <= 0f) {
                control.minimum
            } else {
                (20.0 * log10(volume.toDouble())).toFloat().coerceIn(control.minimum, control.maximum)
            }
        }
    }
}
