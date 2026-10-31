package dev.stashy.metadata.github.collect

import ContributionGraph
import RepoMeta
import UserMeta
import dev.stashy.metadata.github.GitHubApiConfig
import dev.stashy.metadata.github.api.GitHubClient
import dev.stashy.metadata.metadataJson
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

private const val PERIOD_DAYS = 365 * 2

internal data class CollectedGitHubMeta(
    val user: UserMeta,
    val repos: RepoMeta,
) {
    internal fun writeTo(directory: Path, json: Json = metadataJson) {
        directory.createDirectories()
        directory.resolve("user.json").writeText(json.encodeToString(user))
        directory.resolve("repo.json").writeText(json.encodeToString(repos))
    }
}

internal suspend fun collectMeta(config: GitHubApiConfig): CollectedGitHubMeta {
    val overrides = loadRepoOverrides()
    return GitHubClient(config).use { client ->
        collect(
            config = config,
            client = client,
            descriptionOverrides = overrides.descriptions,
        )
    }
}

internal suspend fun collect(
    config: GitHubApiConfig,
    client: GitHubClient,
    descriptionOverrides: Map<String, String> = emptyMap(),
    now: Instant = Clock.System.now(),
): CollectedGitHubMeta {
    val periodFrom = now - PERIOD_DAYS.days
    val profile = client.fetchProfile()
    val contributions = fetchMergedContributions(client, periodFrom, now)

    return CollectedGitHubMeta(
        user = UserMeta(
            generatedAt = now,
            profile = profile.toProfileMeta(),
            contributionGraph = ContributionGraph(
                from = periodFrom,
                to = now,
                totalContributions = contributions.totalContributions,
                days = contributions.days,
            ),
            languageShare = buildLanguageShare(
                config = config,
                login = profile.login,
                contributions = contributions.commitContributions,
            ),
        ),
        repos = buildRepoMeta(
            profile = profile,
            config = config,
            descriptionOverrides = descriptionOverrides,
            now = now,
        ),
    )
}
