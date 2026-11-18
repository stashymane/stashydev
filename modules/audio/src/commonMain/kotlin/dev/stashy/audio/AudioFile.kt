package dev.stashy.audio

import kotlin.time.Duration

/**
 * A loaded, decoded audio track. [openStream] returns a fresh, independent, seekable
 * stream over the file's raw bytes, so seeking is just opening a new stream and seeking it.
 */
public interface AudioFile : AutoCloseable {
    public val format: AudioFormat

    /** Total duration, or [Duration.ZERO] when unknown. */
    public val duration: Duration

    public suspend fun openStream(): AudioStream

    override fun close()

    public companion object {
        public suspend fun load(
            source: AudioSource,
            loader: AudioSourceLoader = defaultAudioLoader(),
        ): Result<AudioFile> = decodeAudio(source, loader)

        public suspend fun load(
            url: String,
            loader: AudioSourceLoader = defaultAudioLoader(),
        ): Result<AudioFile> = load(AudioSource.Url(url), loader)
    }
}

internal expect suspend fun decodeAudio(source: AudioSource, loader: AudioSourceLoader): Result<AudioFile>
