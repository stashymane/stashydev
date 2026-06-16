package ui

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*

val httpClient: HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.INFO
    }
    install(Resources)
    install(ContentNegotiation) {
    }
}

