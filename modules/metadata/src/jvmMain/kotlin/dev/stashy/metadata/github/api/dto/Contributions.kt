package dev.stashy.metadata.github.api.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ContributionsQueryData(val user: GqlUserContributions?)

@Serializable
internal data class ContributionsQueryVariables(
    val login: String,
    val from: String,
    val to: String,
)

@Serializable
internal data class GqlUserContributions(
    val contributionsCollection: ContributionsCollection,
)

@Serializable
internal data class ContributionsCollection(
    val contributionCalendar: ContributionCalendar,
    val commitContributionsByRepository: List<CommitContributionsByRepository>,
)

@Serializable
internal data class ContributionCalendar(
    val totalContributions: Int,
    val weeks: List<ContributionWeek>,
)

@Serializable
internal data class ContributionWeek(
    val contributionDays: List<GqlContributionDay>,
)

@Serializable
internal data class GqlContributionDay(
    val date: String,
    val contributionCount: Int,
)

@Serializable
internal data class CommitContributionsByRepository(
    val contributions: CountConnection,
    val repository: ContributedRepository,
)

@Serializable
internal data class ContributedRepository(
    val nameWithOwner: String,
    val isFork: Boolean,
    val isArchived: Boolean,
    val primaryLanguage: LanguageName? = null,
    val languages: LanguageConnection? = null,
)
