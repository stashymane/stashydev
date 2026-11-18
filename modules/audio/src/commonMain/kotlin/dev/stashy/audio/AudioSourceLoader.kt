package dev.stashy.audio

/**
 * Opens a seekable [AudioStream] for an [AudioSource]. Implementations may be backed by
 * Ktor, platform HTTP clients, the filesystem, or anything else.
 */
public fun interface AudioSourceLoader {
    public suspend fun open(source: AudioSource): Result<AudioStream>
}

/** The platform default loader, used when [AudioFile.load] is called without an explicit loader. */
public expect fun defaultAudioLoader(): AudioSourceLoader
