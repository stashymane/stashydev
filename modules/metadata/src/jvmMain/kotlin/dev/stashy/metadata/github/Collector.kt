package dev.stashy.metadata.github

import ContributionGraph
import ProfileMeta
import ReleaseMeta
import RepoMeta
import RepositoryMeta
import UserMeta
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

/** GitHub rejects contributionsCollection windows longer than one year. */
private val MaxContributionWindow: Duration = 365.days

private const val PERIOD_DAYS = 365 * 2

internal data class CollectedGitHubMeta(
    val user: UserMeta,
    val repos: RepoMeta,
)

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
    val contributions = contributionWindows(periodFrom, now).map { (from, to) ->
        client.fetchContributions(from.toString(), to.toString())
    }
    val merged = mergeContributions(contributions)

    return CollectedGitHubMeta(
        user = UserMeta(
            generatedAt = now,
            profile = profile.toProfileMeta(),
            contributionGraph = ContributionGraph(
                from = periodFrom,
                to = now,
                totalContributions = merged.totalContributions,
                days = merged.days,
            ),
            languageShare = buildLanguageShare(
                config = config,
                login = profile.login,
                contributions = merged.commitContributions,
            ),
        ),
        repos = run {
            val pinned = profile.pinnedItems.nodes
                .filterNotNull()
                .map(GqlRepository::toRepositoryMeta)
            val pinnedFullNames = pinned.map { it.fullName }.toSet()

            RepoMeta(
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
        },
    )
}

/**
 * Splits `[from, to]` into windows of at most [MaxContributionWindow].
 * Adjacent windows share the boundary instant; day counts are identical there so merging is safe.
 */
internal fun contributionWindows(from: Instant, to: Instant): List<Pair<Instant, Instant>> {
    require(from <= to) { "from ($from) must be <= to ($to)" }
    val windows = ArrayList<Pair<Instant, Instant>>()
    var windowTo = to
    while (true) {
        val windowFrom = maxOf(from, windowTo - MaxContributionWindow)
        windows += windowFrom to windowTo
        if (windowFrom <= from) break
        windowTo = windowFrom
    }
    windows.reverse()
    return windows
}

private data class MergedContributions(
    val totalContributions: Int,
    val days: Map<String, Int>,
    val commitContributions: List<CommitContributionsByRepository>,
)

private fun mergeContributions(
    collections: List<ContributionsCollection>,
): MergedContributions {
    val days = linkedMapOf<String, Int>()
    val commitsByRepo = linkedMapOf<String, Pair<Int, ContributedRepository>>()

    for (collection in collections) {
        for (day in collection.contributionCalendar.weeks.flatMap { it.contributionDays }) {
            days[day.date] = day.contributionCount
        }
        for (entry in collection.commitContributionsByRepository) {
            val key = entry.repository.nameWithOwner
            val prior = commitsByRepo[key]
            commitsByRepo[key] = if (prior == null) {
                entry.contributions.totalCount to entry.repository
            } else {
                (prior.first + entry.contributions.totalCount) to entry.repository
            }
        }
    }

    return MergedContributions(
        totalContributions = days.values.sum(),
        days = days,
        commitContributions = commitsByRepo.map { (_, value) ->
            CommitContributionsByRepository(
                contributions = CountConnection(value.first),
                repository = value.second,
            )
        },
    )
}

private fun GqlUserProfile.toProfileMeta() = ProfileMeta(
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
