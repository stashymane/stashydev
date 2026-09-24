package ui.preview

import ContributionGraph
import ProfileMeta
import Project
import Project.Status.Unmaintained
import RepoMeta
import RepositoryMeta
import UserMeta
import io.ktor.http.Url
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

object PreviewData {
    val project = Project(
        name = "vintage-story-runner",
        description = "Docker image to download & run a Vintage Story server *(`x64` & `arm64`)*",
        status = Unmaintained,
        urls = listOf(Url("https://github.com/stashymane/vintage-story-runner")),
        created = LocalDate(2026, 1, 1),
        languages = listOf(Kotlin),
        license = "MIT",
        updatedAt = Clock.System.now() - 3.hours
    )

    val repositoryMeta = RepositoryMeta(
        name = "vintage-story-runner",
        fullName = "stashymane/vintage-story-runner",
        description = "Docker image to download & run a Vintage Story server *(`x64` & `arm64`)*",
        htmlUrl = "https://github.com/stashymane/vintage-story-runner",
        homepage = null,
        stars = 12,
        forks = 2,
        watchers = 12,
        openIssues = 0,
        isFork = false,
        isArchived = false,
        isPrivate = false,
        primaryLanguage = "Kotlin",
        languages = mapOf("Kotlin" to 100.0),
        topics = emptyList(),
        license = "MIT",
        createdAt = Clock.System.now() - 100.hours,
        updatedAt = Clock.System.now() - 3.hours,
        pushedAt = Clock.System.now() - 3.hours,
        latestRelease = null,
    )

    val repoMeta = RepoMeta(
        generatedAt = Clock.System.now(),
        pinned = List(4) { repositoryMeta },
        repositories = List(3) { repositoryMeta },
    )

    val userMeta = UserMeta(
        generatedAt = Clock.System.now(),
        profile = ProfileMeta(
            login = "stashymane",
            name = "Albertas Š.",
            bio = "",
            avatarUrl = "",
            htmlUrl = "https://github.com/stashymane",
            company = null,
            location = "Vilnius, Lithuania",
            blog = "https://stashy.dev/",
            publicRepos = 30,
            publicGists = 0,
            followers = 13,
            following = 15,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
        ),
        contributionGraph = ContributionGraph(
            from = Clock.System.now(),
            to = Clock.System.now(),
            totalContributions = 962,
            days = mapOf(
                "2026-01-01" to 2,
                "2026-01-02" to 8,
                "2026-01-03" to 4,
                "2026-01-04" to 0,
                "2026-01-05" to 12,
            ),
        ),
        languageShare = mapOf(
            "Kotlin" to 61.7,
            "Rust" to 37.3,
            "HTML" to 0.7,
        ),
    )
}
