package ui.preview

import Project
import Project.Language.Kotlin
import Project.Status.Unmaintained
import io.ktor.http.*
import kotlinx.datetime.LocalDate

object PreviewData {
    val project = Project(
        name = "vintage-story-runner",
        description = "Docker image to download & run a Vintage Story server (x64 & arm64)",
        status = Unmaintained,
        urls = listOf(Url("https://github.com/stashymane/vintage-story-runner")),
        created = LocalDate(2026, 1, 1),
        languages = listOf(Kotlin)
    )
}
