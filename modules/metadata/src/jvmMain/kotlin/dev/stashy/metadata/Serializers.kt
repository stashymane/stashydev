package dev.stashy.metadata

import kotlinx.serialization.json.Json

val metadataJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    prettyPrint = false
}
