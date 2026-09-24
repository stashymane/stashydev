package dev.stashy.metadata.github

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int

class GitHubCommand : SuspendingCliktCommand("github") {
    init {
        subcommands(GitHubCollectCommand(), GitHubDevCommand())
    }

    override fun help(context: Context) = "GitHub profile and repository metadata"

    override suspend fun run() = Unit
}

abstract class GitHubSubcommand(name: String) : SuspendingCliktCommand(name) {
    val token: String by option(envvar = "GITHUB_TOKEN", help = "GitHub API token").required()
    val username: String by option(help = "username to fetch data for").default("stashymane")

    val repoLimit: Int by option(help = "limit of latest repositories").int().default(16)
    val includeForks: Boolean by option(help = "should forks be included").flag()
    val includeArchived: Boolean by option(help = "should archived repos be included").flag()

    val apiUrl: String by option().default("https://api.github.com")

    protected fun paramsToConfig(): GitHubApiConfig = GitHubApiConfig(
        token = token,
        username = username,
        repoLimit = repoLimit,
        includeForks = includeForks,
        includeArchived = includeArchived,
        apiUrl = apiUrl,
    )
}
