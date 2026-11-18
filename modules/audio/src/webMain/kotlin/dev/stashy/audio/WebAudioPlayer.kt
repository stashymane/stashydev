package dev.stashy.audio

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.await
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLAudioElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public actual fun AudioPlayer(): AudioPlayer = WebAudioPlayer()

/**
 * Plays through an [HTMLAudioElement], letting the browser stream the source progressively
 * (HTTP range requests) with bounded memory. Seek sets [HTMLAudioElement.currentTime], so
 * seek granularity is whatever the browser provides.
 */
internal class WebAudioPlayer : AudioPlayer {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val element = document.createElement("audio") as HTMLAudioElement

    private val _state = MutableStateFlow(AudioPlayer.State.Idle)
    private val _position = MutableStateFlow(Duration.ZERO)
    override val state: StateFlow<AudioPlayer.State> = _state
    override val position: StateFlow<Duration> = _position

    override var volume: Float
        get() = element.volume.toFloat()
        set(value) {
            element.volume = value.coerceIn(0f, 1f).toDouble()
        }

    init {
        element.addEventListener("timeupdate") { _position.value = element.currentTime.seconds }
        element.addEventListener("ended") { _state.value = AudioPlayer.State.Ended }
        element.addEventListener("error") {
            if (_state.value != AudioPlayer.State.Idle) _state.value = AudioPlayer.State.Failed
        }
        element.addEventListener("pause") {
            if (_state.value == AudioPlayer.State.Playing) _state.value = AudioPlayer.State.Paused
        }
        element.addEventListener("play") {
            if (_state.value == AudioPlayer.State.Loading || _state.value == AudioPlayer.State.Paused) {
                _state.value = AudioPlayer.State.Playing
            }
        }
    }

    override suspend fun play(track: AudioFile) {
        stop()
        val file = track as? WebAudioFile
        if (file == null) {
            _state.value = AudioPlayer.State.Failed
            return
        }
        _state.value = AudioPlayer.State.Loading
        element.src = file.url
        try {
            element.play().await()
            _state.value = AudioPlayer.State.Playing
        } catch (t: Throwable) {
            _state.value = AudioPlayer.State.Failed
        }
    }

    override fun pause() {
        element.pause()
        if (_state.value == AudioPlayer.State.Playing) _state.value = AudioPlayer.State.Paused
    }

    override fun resume() {
        scope.launch {
            try {
                element.play().await()
                _state.value = AudioPlayer.State.Playing
            } catch (t: Throwable) {
                _state.value = AudioPlayer.State.Failed
            }
        }
    }

    override fun stop() {
        element.pause()
        element.currentTime = 0.0
        _position.value = Duration.ZERO
        _state.value = AudioPlayer.State.Stopped
    }

    override suspend fun seek(position: Duration) {
        element.currentTime = position.inWholeSeconds.toDouble()
        _position.value = position
    }

    override fun close() {
        element.pause()
        element.removeAttribute("src")
        scope.cancel()
    }
}
