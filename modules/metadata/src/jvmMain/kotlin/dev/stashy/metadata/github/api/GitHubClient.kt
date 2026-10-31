package dev.stashy.metadata.github.api

import dev.stashy.metadata.github.GitHubApiConfig
import dev.stashy.metadata.github.api.dto.ContributionsCollection
import dev.stashy.metadata.github.api.dto.ContributionsQueryData
import dev.stashy.metadata.github.api.dto.ContributionsQueryVariables
import dev.stashy.metadata.github.api.dto.GqlUserProfile
import dev.stashy.metadata.github.api.dto.GraphqlResponse
import dev.stashy.metadata.github.api.dto.ProfileQueryData
import dev.stashy.metadata.github.api.dto.ProfileQueryVariables
import dev.stashy.metadata.github.api.dto.RestContributor
import dev.stashy.metadata.github.api.dto.RestContributorStats
import dev.stashy.metadata.metadataJson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import kotlin.time.Duration.Companion.seconds

internal class GitHubClient(
    private val config: GitHubApiConfig,
) : AutoCloseable {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(metadataJson)
        }

        install(Logging) {
            level = LogLevel.INFO
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30.seconds.inWholeMilliseconds
            connectTimeoutMillis = 10.seconds.inWholeMilliseconds
            socketTimeoutMillis = 30.seconds.inWholeMilliseconds
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
            retryIf { _, response -> response.status == HttpStatusCode.Forbidden }
        }

        defaultRequest {
            url(config.apiUrl)

            accept(ContentType("application", "vnd.github+json"))
            header("X-GitHub-Api-Version", "2026-03-10")
            header(HttpHeaders.UserAgent, "stashydev-githubMeta")
            header(HttpHeaders.Authorization, "Bearer ${config.token}")
        }
    }

    override fun close() {
        client.close()
    }

    suspend fun fetchProfile(): GqlUserProfile {
        val repoFirst = (config.repoLimit * 2).coerceAtMost(100)
        return graphql<ProfileQueryData>(
            query = GitHubQueries.profileQuery,
            variables = metadataJson.encodeToJsonElement(
                ProfileQueryVariables(
                    login = config.username,
                    repoFirst = repoFirst,
                ),
            ),
        ).user ?: error("GitHub user '${config.username}' not found via GraphQL")
    }

    suspend fun fetchContributions(fromIso: String, toIso: String): ContributionsCollection {
        return graphql<ContributionsQueryData>(
            query = GitHubQueries.contributionsQuery,
            variables = metadataJson.encodeToJsonElement(
                ContributionsQueryVariables(
                    login = config.username,
                    from = fromIso,
                    to = toIso,
                ),
            ),
        ).user?.contributionsCollection
            ?: error("GitHub user '${config.username}' not found via GraphQL")
    }

    suspend fun fetchReadme(fullName: String): String? {
        val response = client.get("/repos/$fullName/readme") {
            accept(ContentType("application", "vnd.github.raw"))
        }
        if (response.status == HttpStatusCode.NotFound) return null
        return response.requireSuccess().bodyAsText()
    }

    suspend fun fetchContributors(fullName: String): List<RestContributor> {
        val contributors = mutableListOf<RestContributor>()
        var page = 1
        while (true) {
            val response = client.get("/repos/$fullName/contributors") {
                parameter("per_page", 100)
                parameter("page", page)
            }
            if (response.status == HttpStatusCode.NoContent) break
            contributors += response.requireSuccess().body<List<RestContributor>>()
            if (!response.hasNextPage()) break
            page++
        }
        return contributors
    }

    suspend fun fetchRecentContributorStats(fullName: String): List<RestContributorStats> {
        repeat(3) { attempt ->
            val response = client.get("/repos/$fullName/stats/contributors")
            when (response.status) {
                HttpStatusCode.Accepted -> if (attempt < 2) delay(2.seconds)
                HttpStatusCode.NoContent -> return emptyList()
                else -> return response.requireSuccess().body<List<RestContributorStats>>()
            }
        }
        return emptyList()
    }

    private suspend inline fun <reified T> graphql(
        query: String,
        variables: JsonElement,
    ): T {
        val response: GraphqlResponse<T> = client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody(
                buildJsonObject {
                    put("query", query)
                    put("variables", variables)
                },
            )
        }.requireSuccess().body()

        if (!response.errors.isNullOrEmpty()) {
            val messages = response.errors.joinToString("; ") { it.message }
            error("GitHub GraphQL error: $messages")
        }

        return response.data ?: error("GitHub GraphQL returned no data")
    }
}

private suspend fun HttpResponse.requireSuccess(): HttpResponse {
    if (!status.isSuccess()) {
        val body = runCatching { body<String>() }.getOrDefault("<unreadable body>")
        error("GitHub API ${status.value} ${status.description}: $body")
    }
    return this
}

private fun HttpResponse.hasNextPage(): Boolean =
    headers[HttpHeaders.Link]?.contains("rel=\"next\"") == true
