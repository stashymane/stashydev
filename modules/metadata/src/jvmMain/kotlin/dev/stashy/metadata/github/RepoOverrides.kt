package dev.stashy.metadata.github

import dev.stashy.metadata.json
import kotlinx.serialization.Serializable

@Serializable
internal data class RepoOverrides(
    val descriptions: Map<String, String> = emptyMap(),
)

internal fun loadRepoOverrides(): RepoOverrides {
    val stream = RepoOverrides::class.java.getResourceAsStream("/repo-overrides.json")
        ?: return RepoOverrides()
    return stream.bufferedReader().use { reader ->
        json.decodeFromString(RepoOverrides.serializer(), reader.readText())
    }
}
