package dev.stashy.audio

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit

public actual fun defaultAudioLoader(): AudioSourceLoader = FetchAudioLoader

internal object FetchAudioLoader : AudioSourceLoader {
    override suspend fun open(source: AudioSource): Result<AudioStream> = runCatching {
        when (source) {
            is AudioSource.Bytes -> ByteArrayAudioStream(source.data)
            is AudioSource.Url -> RangeAudioStream(source.url, source.headers)
        }
    }
}

internal actual suspend fun httpRangeRequest(
    url: String,
    headers: Map<String, String>,
    start: Long,
    end: Long,
): HttpRangeResponse {
    val requestHeaders = Headers()
    headers.forEach { (key, value) -> requestHeaders.append(key, value) }
    requestHeaders.append("Range", "bytes=$start-$end")
    val response = window.fetch(url, RequestInit(method = "GET", headers = requestHeaders)).await()
    if (!response.ok && response.status != 206.toShort()) {
        throw IllegalStateException("HTTP ${response.status} for $url")
    }
    val data = response.arrayBuffer().await().toByteArray()
    val contentRange = response.headers.get("Content-Range")
    val totalSize = contentRange
        ?.substringAfterLast('/')
        ?.takeIf { it != "*" }
        ?.toLongOrNull()
        ?: response.headers.get("Content-Length")?.toLongOrNull()
        ?: -1L
    val dataStart = if (response.status == 206.toShort()) {
        contentRange?.substringAfter("bytes ")?.substringBefore('-')?.toLongOrNull() ?: start
    } else {
        0L
    }
    return HttpRangeResponse(data, dataStart, totalSize)
}
