package dev.stashy.metadata.github.collect

import dev.stashy.metadata.github.GitHubApiConfig
import dev.stashy.metadata.github.api.dto.GqlRepository
import dev.stashy.metadata.github.api.dto.GqlUserProfile
import RepoMeta
import kotlin.time.Instant

internal fun buildRepoMeta(
    profile: GqlUserProfile,
    config: GitHubApiConfig,
    descriptionOverrides: Map<String, String>,
    now: Instant,
): RepoMeta {
    val pinned = profile.pinnedItems.nodes
        .filterNotNull()
        .map(GqlRepository::toRepositoryMeta)
    val pinnedFullNames = pinned.map { it.fullName }.toSet()

    return RepoMeta(
        generatedAt = now,
        pinned = pinned,
        repositories = profile.repositories.nodes
            .asSequence()
            .filter { it.name != profile.login }
            .filter { it.nameWithOwner !in pinnedFullNames }
            .filter { config.includeForks || !it.isFork }
            .filter { config.includeArchived || !it.isArchived }
            .take(config.repoLimit)
            .map { it.toRepositoryMeta() }
            .toList(),
        descriptionOverrides = descriptionOverrides,
    )
}
