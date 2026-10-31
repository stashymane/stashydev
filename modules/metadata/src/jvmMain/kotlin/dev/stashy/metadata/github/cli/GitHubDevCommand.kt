package dev.stashy.metadata.github.cli

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import dev.stashy.metadata.github.collect.collectMeta
import dev.stashy.metadata.metadataJson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists

class GitHubDevCommand : GitHubSubcommand("dev") {
    val port: Int by option(help = "local server port").int().default(8080)
    val host: String by option(help = "local server host").default("127.0.0.1")
    val output: String by option(help = "static directory for user.json and repo.json")
        .default("build/metadata")
    val force: Boolean by option(help = "collect metadata even if the output directory already exists")
        .flag()

    override fun help(context: Context) =
        "Collect GitHub metadata into a static directory and serve it at /api/"

    override suspend fun run() {
        val config = paramsToConfig()
        val outDir = Path(output).absolute().normalize()

        if (force || !outDir.exists()) {
            echo("Collecting GitHub metadata for '${config.username}'...")
            collectMeta(config).writeTo(outDir, json = Json(from = metadataJson) { prettyPrint = true })
        } else {
            echo("Using existing metadata from $outDir")
        }

        echo("Serving metadata at http://$host:$port/api/")
        embeddedServer(CIO, port = port, host = host) {
            install(CORS) {
                anyHost()
                allowHeader(HttpHeaders.ContentType)
                allowMethod(HttpMethod.Get)
                allowMethod(HttpMethod.Options)
            }

            routing {
                staticFiles("/api", outDir.toFile())
            }
        }.startSuspend(wait = true)
    }
}
