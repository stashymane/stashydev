package dev.stashy.audio

import kotlinx.browser.document
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.dom.HTMLAudioElement
import kotlin.coroutines.resume
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal actual suspend fun decodeAudio(source: AudioSource, loader: AudioSourceLoader): Result<AudioFile> =
    runCatching {
        when (source) {
            is AudioSource.Url -> WebAudioFile(
                source = source,
                loader = loader,
                url = source.url,
                duration = probeDuration(source.url),
                onClose = null,
            )
            is AudioSource.Bytes -> {
                val blobUrl = source.data.toBlobUrl(source.mimeType ?: "application/octet-stream")
                WebAudioFile(
                    source = source,
                    loader = loader,
                    url = blobUrl,
                    duration = probeDuration(blobUrl),
                    onClose = { revokeBlobUrl(blobUrl) },
                )
            }
        }
    }

/** Reads the duration from the media element's metadata without decoding the whole file. */
private suspend fun probeDuration(url: String): Duration {
    val element = document.createElement("audio") as HTMLAudioElement
    element.preload = "metadata"
    element.src = url
    suspendCancellableCoroutine<Unit> { cont ->
        element.addEventListener("loadedmetadata") { cont.resume(Unit) }
        element.addEventListener("error") { cont.resume(Unit) }
    }
    val duration = element.duration
    return if (duration.isNaN() || duration < 0) Duration.ZERO else duration.seconds
}

/**
 * On the web the file itself is played back by the browser (progressive streaming with range
 * requests, bounded memory even for very large files), so [AudioFile] only carries the playable
 * URL plus metadata. [openStream] still provides raw, seekable byte access through the loader.
 */
internal class WebAudioFile internal constructor(
    val source: AudioSource,
    private val loader: AudioSourceLoader,
    val url: String,
    override val duration: Duration,
    private val onClose: (() -> Unit)?,
) : AudioFile {
    override val format: AudioFormat = AudioFormat.Unknown

    override suspend fun openStream(): AudioStream = loader.open(source).getOrThrow()

    override fun close() {
        onClose?.invoke()
    }
}
