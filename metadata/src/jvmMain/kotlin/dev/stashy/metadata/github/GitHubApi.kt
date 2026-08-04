package dev.stashy.metadata.github

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
internal data class GraphqlRequest(
    val query: String,
    val variables: MetaQueryVariables,
)

@Serializable
internal data class MetaQueryVariables(
    val login: String,
    val from: String,
    val to: String,
    val repoFirst: Int,
)

@Serializable
internal data class GraphqlResponse<T>(
    val data: T? = null,
    val errors: List<GraphqlError>? = null,
)

@Serializable
internal data class GraphqlError(val message: String)

@Serializable
internal data class MetaQueryData(val user: GqlUser?)

@Serializable
internal data class GqlUser(
    val login: String,
    val name: String? = null,
    val bio: String? = null,
    val avatarUrl: String,
    val url: String,
    val company: String? = null,
    val location: String? = null,
    val websiteUrl: String? = null,
    val twitterUsername: String? = null,
    val publicRepositories: CountConnection,
    val publicGists: CountConnection,
    val followers: CountConnection,
    val following: CountConnection,
    val createdAt: Instant,
    val updatedAt: Instant,
    val repositories: RepositoryConnection,
    val contributionsCollection: ContributionsCollection,
)

@Serializable
internal data class CountConnection(val totalCount: Int)

@Serializable
internal data class RepositoryConnection(val nodes: List<GqlRepository>)

@Serializable
internal data class GqlRepository(
    val name: String,
    val nameWithOwner: String,
    val description: String? = null,
    val url: String,
    val homepageUrl: String? = null,
    val stargazerCount: Int,
    val forkCount: Int,
    val watchers: CountConnection,
    val openIssues: CountConnection,
    val isFork: Boolean,
    val isArchived: Boolean,
    val isPrivate: Boolean,
    val primaryLanguage: LanguageName? = null,
    val languages: LanguageConnection? = null,
    val repositoryTopics: TopicConnection? = null,
    val licenseInfo: LicenseInfo? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
    val pushedAt: Instant? = null,
    val latestRelease: GqlRelease? = null,
)

@Serializable
internal data class TopicConnection(val nodes: List<RepositoryTopic>)

@Serializable
internal data class RepositoryTopic(val topic: TopicName)

@Serializable
internal data class TopicName(val name: String)

@Serializable
internal data class LicenseInfo(
    val spdxId: String? = null,
    val name: String? = null,
)

@Serializable
internal data class GqlRelease(
    val tagName: String,
    val name: String? = null,
    val url: String,
    val publishedAt: Instant? = null,
    val isPrerelease: Boolean = false,
    val isDraft: Boolean = false,
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

@Serializable
internal data class LanguageName(val name: String)

@Serializable
internal data class LanguageConnection(
    val edges: List<LanguageEdge>,
)

@Serializable
internal data class LanguageEdge(
    val size: Long,
    val node: LanguageName,
)
