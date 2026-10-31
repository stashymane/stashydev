package dev.stashy.metadata.github.collect

import dev.stashy.metadata.Resources
import dev.stashy.metadata.metadataJson
import kotlinx.serialization.Serializable

@Serializable
internal data class RepoOverrides(
    val descriptions: Map<String, String> = emptyMap(),
)

internal fun loadRepoOverrides(): RepoOverrides {
    val text = Resources.readText("/repo-overrides.json") ?: return RepoOverrides()
    return metadataJson.decodeFromString(RepoOverrides.serializer(), text)
}
