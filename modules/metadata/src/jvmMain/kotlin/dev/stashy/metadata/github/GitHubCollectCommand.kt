package dev.stashy.metadata.github

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import dev.stashy.metadata.metadataJson
import kotlin.io.path.*

class GitHubCollectCommand : GitHubSubcommand("collect") {
    val output: String by option(help = "output directory for user.json and repo.json").required()

    override fun help(context: Context) = "Fetch GitHub metadata and write user.json / repo.json"

    override suspend fun run() {
        val config = paramsToConfig()
        val outDir = Path(output).absolute().normalize()
        if (!outDir.isDirectory()) {
            outDir.createDirectories()
        }

        val meta = collectMeta(config)
        val userPath = outDir.resolve("user.json")
        val repoPath = outDir.resolve("repo.json")
        userPath.writeText(metadataJson.encodeToString(meta.user))
        repoPath.writeText(metadataJson.encodeToString(meta.repos))
        echo("Wrote GitHub metadata for '${config.username}' to $userPath and $repoPath")
    }
}
