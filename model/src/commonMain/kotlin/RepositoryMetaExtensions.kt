import io.ktor.http.Url
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun RepoMeta.featuredProjects(): List<Project> =
    pinned.map { it.toProject(descriptionOverrides[it.name]) }

fun RepoMeta.latestProjects(): List<Project> =
    repositories.map { it.toProject(descriptionOverrides[it.name]) }

fun RepositoryMeta.toProject(descriptionOverride: String? = null): Project {
    val languageNames = languages
        .filterValues { it >= 5.0 }
        .entries
        .sortedByDescending { it.value }
        .map { it.key }

    return Project(
        name = name,
        description = descriptionOverride ?: description,
        status = when {
            isArchived -> Project.Status.Archived
            else -> Project.Status.Active
        },
        created = createdAt.toLocalDateTime(TimeZone.UTC).date,
        languages = languageNames.map { Project.Language.fromLabel(it) },
        urls = buildList {
            homepage?.takeIf(String::isNotBlank)?.let { add(Url(it)) }
            add(Url(htmlUrl))
        },
        license = license,
        updatedAt = updatedAt
    )
}
