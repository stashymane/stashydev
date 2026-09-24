package data

import dev.stashy.home.BuildKonfig
import httpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object StaticApi {
    internal fun url(fileName: String): Url = Url("${BuildKonfig.STATIC_BASE_URL}/api/$fileName")

    suspend fun fetch(fileName: String): String =
        httpClient.get(url(fileName)).bodyAsText()
}
