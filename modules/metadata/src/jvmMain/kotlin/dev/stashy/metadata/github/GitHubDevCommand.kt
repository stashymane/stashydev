package dev.stashy.metadata.github

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import dev.stashy.metadata.metadataJson
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class GitHubDevCommand : GitHubSubcommand("dev") {
    val port: Int by option(help = "local server port").int().default(8080)
    val host: String by option(help = "local server host").default("127.0.0.1")

    override fun help(context: Context) =
        "Fetch GitHub metadata and serve it at /api/user.json and /api/repo.json"

    override suspend fun run() {
        val config = paramsToConfig()
        echo("Collecting GitHub metadata for '${config.username}'…")
        val meta = collectMeta(config)

        echo("Serving metadata at http://$host:$port/api/")
        embeddedServer(CIO, port = port, host = host) {
            install(ContentNegotiation) {
                json(metadataJson)
            }
            
            install(CORS) {
                anyHost()
                allowHeader(HttpHeaders.ContentType)
                allowMethod(HttpMethod.Get)
                allowMethod(HttpMethod.Options)
            }

            routing {
                get("/api/user.json") {
                    call.respond(meta.user)
                }
                get("/api/repo.json") {
                    call.respond(meta.repos)
                }
            }
        }.startSuspend(wait = true)
    }
}
