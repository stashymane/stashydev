package dev.stashy.audio

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

public interface AudioPlayer : AutoCloseable {
    public enum class State {
        Idle,
        Loading,
        Playing,
        Paused,
        Stopped,
        Ended,
        Failed,
    }

    public val state: StateFlow<State>

    public val position: StateFlow<Duration>

    /** Volume in the range 0..1. */
    public var volume: Float

    public suspend fun play(track: AudioFile)

    public fun pause()

    public fun resume()

    public fun stop()

    public suspend fun seek(position: Duration)

    override fun close()
}

public expect fun AudioPlayer(): AudioPlayer
