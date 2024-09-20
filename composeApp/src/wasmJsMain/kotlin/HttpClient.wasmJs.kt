import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*

actual val httpClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
    }
    install(Resources)
    install(ContentNegotiation) {
        json(json)
    }
}