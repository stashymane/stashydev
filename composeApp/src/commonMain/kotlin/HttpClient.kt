import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*

val httpClient: HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.INFO
    }
    install(Resources)
    install(ContentNegotiation) {
        json(json)
    }
}

