package dev.stashy.audio

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

public actual fun defaultAudioLoader(): AudioSourceLoader = JvmAudioLoader

internal object JvmAudioLoader : AudioSourceLoader {
    override suspend fun open(source: AudioSource): Result<AudioStream> = runCatching {
        when (source) {
            is AudioSource.Bytes -> ByteArrayAudioStream(source.data)
            is AudioSource.Url -> openUrl(source)
        }
    }

    private suspend fun openUrl(source: AudioSource.Url): AudioStream = withContext(Dispatchers.IO) {
        val url = URI(source.url).toURL()
        if (url.protocol == "file") {
            FileAudioStream(java.io.File(url.toURI()))
        } else {
            RangeAudioStream(source.url, source.headers)
        }
    }
}

internal actual suspend fun httpRangeRequest(
    url: String,
    headers: Map<String, String>,
    start: Long,
    end: Long,
): HttpRangeResponse = withContext(Dispatchers.IO) {
    val client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build()
    val request = HttpRequest.newBuilder(URI(url))
        .GET()
        .header("Range", "bytes=$start-$end")
        .apply { headers.forEach { (key, value) -> header(key, value) } }
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())
    val contentRange = response.headers().firstValue("Content-Range").orElse(null)
    val totalSize = contentRange
        ?.substringAfterLast('/')
        ?.takeIf { it != "*" }
        ?.toLongOrNull()
        ?: response.headers().firstValue("Content-Length").orElse(null)?.toLongOrNull()
        ?: -1L
    val dataStart = if (response.statusCode() == 206) {
        contentRange?.substringAfter("bytes ")?.substringBefore('-')?.toLongOrNull() ?: start
    } else {
        0L
    }
    HttpRangeResponse(response.body(), dataStart, totalSize)
}
