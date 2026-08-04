import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class UserMeta(
    val generatedAt: Instant,
    val profile: ProfileMeta,
    val contributionGraph: ContributionGraph,
    val languageShare: Map<String, Double>,
)

@Serializable
data class RepoMeta(
    val generatedAt: Instant,
    val repositories: List<RepositoryMeta>,
)

@Serializable
data class ProfileMeta(
    val login: String,
    val name: String?,
    val bio: String?,
    val avatarUrl: String,
    val htmlUrl: String,
    val company: String?,
    val location: String?,
    val blog: String?,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
data class RepositoryMeta(
    val name: String,
    val fullName: String,
    val description: String?,
    val htmlUrl: String,
    val homepage: String?,
    val stars: Int,
    val forks: Int,
    val watchers: Int,
    val openIssues: Int,
    val isFork: Boolean,
    val isArchived: Boolean,
    val isPrivate: Boolean,
    val primaryLanguage: String?,
    val languages: Map<String, Long>,
    val topics: List<String>,
    val license: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val pushedAt: Instant?,
    val latestRelease: ReleaseMeta?,
)

@Serializable
data class ReleaseMeta(
    val tagName: String,
    val name: String?,
    val htmlUrl: String,
    val publishedAt: Instant?,
    val isPrerelease: Boolean,
    val isDraft: Boolean,
)

@Serializable
data class ContributionGraph(
    val from: Instant,
    val to: Instant,
    val totalContributions: Int,
    val days: Map<String, Int>,
)
