package dev.stashy.metadata.github

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import dev.stashy.metadata.json
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.writeText

class GitHubCommand : SuspendingCliktCommand("github") {
    val output: String by argument(help = "output directory for user.json and repo.json").default(".")

    val token: String by option(envvar = "GITHUB_TOKEN", help = "GitHub API token").required()
    val username: String by option(help = "username to fetch data for").default("stashymane")

    val repoLimit: Int by option(help = "limit of latest repositories").int().default(16)
    val includeForks: Boolean by option(help = "should forks be included").flag()
    val includeArchived: Boolean by option(help = "should archived repos be included").flag()

    val apiUrl: String by option().default("https://api.github.com")

    fun paramsToConfig(): GitHubApiConfig = GitHubApiConfig(
        output = output,
        token = token,
        username = username,
        repoLimit = repoLimit,
        includeForks = includeForks,
        includeArchived = includeArchived,
        apiUrl = apiUrl,
    )

    override suspend fun run() {
        val config = paramsToConfig()
        val outDir = Path(config.output).absolute().normalize()
        if (!outDir.isDirectory()) {
            outDir.createDirectories()
        }

        GitHubClient(config).use { client ->
            val meta = collect(config, client)
            val userPath = outDir.resolve("user.json")
            val repoPath = outDir.resolve("repo.json")
            userPath.writeText(json.encodeToString(meta.user))
            repoPath.writeText(json.encodeToString(meta.repos))
            echo("Wrote GitHub metadata for '${config.username}' to $userPath and $repoPath")
        }
    }
}
