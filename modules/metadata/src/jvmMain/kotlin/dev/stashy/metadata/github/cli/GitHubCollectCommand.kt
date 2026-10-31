package dev.stashy.metadata.github.cli

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import dev.stashy.metadata.github.collect.collectMeta
import kotlin.io.path.Path
import kotlin.io.path.absolute

class GitHubCollectCommand : GitHubSubcommand("collect") {
    val output: String by option(help = "output directory for user.json and repo.json").required()

    override fun help(context: Context) = "Fetch GitHub metadata and write user.json / repo.json"

    override suspend fun run() {
        val config = paramsToConfig()
        val outDir = Path(output).absolute().normalize()
        val files = collectMeta(config).writeTo(outDir)
        echo("Wrote GitHub metadata for '${config.username}' to ${outDir.toAbsolutePath().normalize()}")
    }
}
