package ui

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import json

val httpClient: HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.INFO
    }
    install(Resources)
    install(ContentNegotiation) {
        json(json)
    }
}

