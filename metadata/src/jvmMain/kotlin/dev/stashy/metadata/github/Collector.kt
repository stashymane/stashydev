package dev.stashy.metadata.github

import ContributionGraph
import ProfileMeta
import ReleaseMeta
import RepoMeta
import RepositoryMeta
import UserMeta
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

private const val PERIOD_DAYS = 365

internal data class CollectedGitHubMeta(
    val user: UserMeta,
    val repos: RepoMeta,
)

internal suspend fun collect(
    config: GitHubApiConfig,
    client: GitHubClient,
    now: Instant = Clock.System.now(),
): CollectedGitHubMeta {
    val periodFrom = now - PERIOD_DAYS.days
    val user = client.fetchMeta(
        fromIso = periodFrom.toString(),
        toIso = now.toString(),
    )

    return CollectedGitHubMeta(
        user = UserMeta(
            generatedAt = now,
            profile = user.toProfileMeta(),
            contributionGraph = ContributionGraph(
                from = periodFrom,
                to = now,
                totalContributions = user.contributionsCollection.contributionCalendar.totalContributions,
                days = user.contributionsCollection.contributionCalendar.weeks
                    .flatMap { it.contributionDays }
                    .associate { it.date to it.contributionCount },
            ),
            languageShare = buildLanguageShare(
                config = config,
                login = user.login,
                contributions = user.contributionsCollection.commitContributionsByRepository,
            ),
        ),
        repos = RepoMeta(
            generatedAt = now,
            repositories = user.repositories.nodes
                .asSequence()
                .filter { it.name != user.login }
                .filter { config.includeForks || !it.isFork }
                .filter { config.includeArchived || !it.isArchived }
                .take(config.repoLimit)
                .map { it.toRepositoryMeta() }
                .toList(),
        ),
    )
}

private fun GqlUser.toProfileMeta() = ProfileMeta(
    login = login,
    name = name,
    bio = bio,
    avatarUrl = avatarUrl,
    htmlUrl = url,
    company = company,
    location = location,
    blog = websiteUrl?.takeIf { it.isNotBlank() },
    publicRepos = publicRepositories.totalCount,
    publicGists = publicGists.totalCount,
    followers = followers.totalCount,
    following = following.totalCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

private fun GqlRepository.toRepositoryMeta() = RepositoryMeta(
    name = name,
    fullName = nameWithOwner,
    description = description,
    htmlUrl = url,
    homepage = homepageUrl?.takeIf { it.isNotBlank() },
    stars = stargazerCount,
    forks = forkCount,
    watchers = watchers.totalCount,
    openIssues = openIssues.totalCount,
    isFork = isFork,
    isArchived = isArchived,
    isPrivate = isPrivate,
    primaryLanguage = primaryLanguage?.name,
    languages = languages?.edges
        ?.takeIf { it.isNotEmpty() }
        ?.let { edges ->
            val total = edges.sumOf { it.size }.coerceAtLeast(1).toDouble()
            edges
                .associate { it.node.name to it.size / total * 100.0 }
                .toList()
                .sortedByDescending { it.second }
                .toMap()
        }
        .orEmpty(),
    topics = repositoryTopics?.nodes?.map { it.topic.name }.orEmpty(),
    license = licenseInfo?.spdxId ?: licenseInfo?.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    pushedAt = pushedAt,
    latestRelease = latestRelease?.let {
        ReleaseMeta(
            tagName = it.tagName,
            name = it.name,
            htmlUrl = it.url,
            publishedAt = it.publishedAt,
            isPrerelease = it.isPrerelease,
            isDraft = it.isDraft,
        )
    },
)

private fun buildLanguageShare(
    config: GitHubApiConfig,
    login: String,
    contributions: List<CommitContributionsByRepository>,
): Map<String, Double> {
    data class Acc(
        var bytes: Long = 0,
        var weightedBytes: Double = 0.0,
    )

    val profileRepo = "$login/$login"
    val byLanguage = linkedMapOf<String, Acc>()

    for (entry in contributions) {
        val repo = entry.repository
        if (repo.nameWithOwner.equals(profileRepo, ignoreCase = true)) continue
        if (!config.includeForks && repo.isFork) continue
        if (!config.includeArchived && repo.isArchived) continue

        val commits = entry.contributions.totalCount
        if (commits <= 0) continue

        val edges = repo.languages?.edges.orEmpty()
        if (edges.isEmpty()) {
            val fallback = repo.primaryLanguage?.name ?: continue
            byLanguage.getOrPut(fallback) { Acc() }.weightedBytes += commits.toDouble()
            continue
        }

        val totalBytes = edges.sumOf { it.size }.coerceAtLeast(1)
        for (edge in edges) {
            val acc = byLanguage.getOrPut(edge.node.name) { Acc() }
            acc.bytes += edge.size
            acc.weightedBytes += edge.size.toDouble() / totalBytes * commits
        }
    }

    val totalWeight = byLanguage.values.sumOf { it.weightedBytes }.takeIf { it > 0 }
        ?: byLanguage.values.sumOf { it.bytes.toDouble() }.coerceAtLeast(1.0)

    return byLanguage
        .mapValues { (_, acc) ->
            val weight = if (acc.weightedBytes > 0) acc.weightedBytes else acc.bytes.toDouble()
            weight / totalWeight * 100.0
        }
        .toList()
        .sortedByDescending { it.second }
        .toMap()
}
