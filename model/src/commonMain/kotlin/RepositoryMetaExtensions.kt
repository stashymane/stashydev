import io.ktor.http.Url
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun RepositoryMeta.toProject(): Project {
    val languageNames = languages
        .filterValues { it >= 5.0 }
        .entries
        .sortedByDescending { it.value }
        .map { it.key }

    return Project(
        name = name,
        description = description,
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
