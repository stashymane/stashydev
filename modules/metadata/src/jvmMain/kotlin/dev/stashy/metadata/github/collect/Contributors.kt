package dev.stashy.metadata.github.collect

import ContributorMeta
import ContributorRef
import RepoMeta
import RepositoryMeta
import dev.stashy.metadata.github.api.GitHubClient

private data class FetchedContributor(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val contributions: Int,
)

private data class RepoContributors(
    val allTime: List<FetchedContributor>,
    val recent: List<FetchedContributor>,
)

internal suspend fun attachContributors(client: GitHubClient, repos: RepoMeta): RepoMeta {
    val targets = repos.pinned + repos.repositories
    val fetched = targets.associate { repo ->
        repo.fullName to fetchRepoContributors(client, repo.fullName)
    }

    val profiles = buildMap<String, ContributorMeta> {
        for (contributors in fetched.values) {
            for (contributor in contributors.allTime + contributors.recent) {
                put(contributor.login, ContributorMeta(contributor.avatarUrl, contributor.htmlUrl))
            }
        }
    }

    fun RepositoryMeta.withContributors(): RepositoryMeta {
        val contributors = fetched.getValue(fullName)
        return copy(
            allTimeContributors = contributors.allTime.map { ContributorRef(it.login, it.contributions) },
            recentContributors = contributors.recent.map { ContributorRef(it.login, it.contributions) },
        )
    }

    return repos.copy(
        pinned = repos.pinned.map { it.withContributors() },
        repositories = repos.repositories.map { it.withContributors() },
        contributors = profiles,
    )
}

private suspend fun fetchRepoContributors(
    client: GitHubClient,
    fullName: String,
): RepoContributors {
    val allTime = client.fetchContributors(fullName)
        .map { FetchedContributor(it.login, it.avatarUrl, it.htmlUrl, it.contributions) }
        .sortedByDescending { it.contributions }

    val recent = client.fetchRecentContributorStats(fullName)
        .mapNotNull { stats ->
            val author = stats.author ?: return@mapNotNull null
            val contributions = stats.weeks.sumOf { it.c }
            if (contributions <= 0) null
            else FetchedContributor(author.login, author.avatarUrl, author.htmlUrl, contributions)
        }
        .sortedByDescending { it.contributions }

    return RepoContributors(allTime, recent)
}
