package dev.stashy.metadata.github.api.dto

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
internal data class ProfileQueryData(val user: GqlUserProfile?)

@Serializable
internal data class ProfileQueryVariables(
    val login: String,
    val repoFirst: Int,
)

@Serializable
internal data class GqlUserProfile(
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
    val pinnedItems: PinnedItemConnection,
    val repositories: RepositoryConnection,
)

@Serializable
internal data class PinnedItemConnection(val nodes: List<GqlRepository?>)

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
