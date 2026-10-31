package dev.stashy.metadata.github.collect

import dev.stashy.metadata.github.api.GitHubClient
import dev.stashy.metadata.github.api.dto.CommitContributionsByRepository
import dev.stashy.metadata.github.api.dto.ContributedRepository
import dev.stashy.metadata.github.api.dto.ContributionsCollection
import dev.stashy.metadata.github.api.dto.CountConnection
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

/** GitHub rejects contributionsCollection windows longer than one year. */
private val MaxContributionWindow: Duration = 365.days

internal data class MergedContributions(
    val totalContributions: Int,
    val days: Map<String, Int>,
    val commitContributions: List<CommitContributionsByRepository>,
)

internal suspend fun fetchMergedContributions(
    client: GitHubClient,
    from: Instant,
    to: Instant,
    concurrency: Int,
): MergedContributions {
    val collections = contributionWindows(from, to).mapConcurrent(concurrency) { (windowFrom, windowTo) ->
        client.fetchContributions(windowFrom.toString(), windowTo.toString())
    }
    return mergeContributions(collections)
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

internal fun mergeContributions(
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
