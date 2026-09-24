package dev.stashy.metadata.github

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

    suspend fun fetchMeta(
        fromIso: String,
        toIso: String,
    ): GqlUser {
        val repoFirst = (config.repoLimit * 2).coerceAtMost(100)

        val response: GraphqlResponse<MetaQueryData> = client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody(
                GraphqlRequest(
                    query = META_QUERY,
                    variables = MetaQueryVariables(
                        login = config.username,
                        from = fromIso,
                        to = toIso,
                        repoFirst = repoFirst,
                    ),
                ),
            )
        }.requireSuccess().body()

        if (!response.errors.isNullOrEmpty()) {
            val messages = response.errors.joinToString("; ") { it.message }
            error("GitHub GraphQL error: $messages")
        }

        return response.data?.user
            ?: error("GitHub user '${config.username}' not found via GraphQL")
    }

    private companion object {
        // language=GraphQL
        private val REPOSITORY_FIELDS = """
            name
            nameWithOwner
            description
            url
            homepageUrl
            stargazerCount
            forkCount
            watchers { totalCount }
            openIssues: issues(states: OPEN) { totalCount }
            isFork
            isArchived
            isPrivate
            primaryLanguage { name }
            languages(first: 20, orderBy: { field: SIZE, direction: DESC }) {
              edges {
                size
                node { name }
              }
            }
            repositoryTopics(first: 20) {
              nodes { topic { name } }
            }
            licenseInfo { spdxId name }
            createdAt
            updatedAt
            pushedAt
            latestRelease {
              tagName
              name
              url
              publishedAt
              isPrerelease
              isDraft
            }
        """.trimIndent()

        // language=GraphQL
        private val META_QUERY = """
            query(${'$'}login: String!, ${'$'}from: DateTime!, ${'$'}to: DateTime!, ${'$'}repoFirst: Int!) {
              user(login: ${'$'}login) {
                login
                name
                bio
                avatarUrl
                url
                company
                location
                websiteUrl
                twitterUsername
                publicRepositories: repositories(privacy: PUBLIC) { totalCount }
                publicGists: gists(privacy: PUBLIC) { totalCount }
                followers { totalCount }
                following { totalCount }
                createdAt
                updatedAt
                pinnedItems(first: 6, types: [REPOSITORY]) {
                  nodes {
                    ... on Repository {
                      $REPOSITORY_FIELDS
                    }
                  }
                }
                repositories(
                  first: ${'$'}repoFirst,
                  orderBy: { field: UPDATED_AT, direction: DESC },
                  ownerAffiliations: OWNER,
                  privacy: PUBLIC
                ) {
                  nodes {
                    $REPOSITORY_FIELDS
                  }
                }
                contributionsCollection(from: ${'$'}from, to: ${'$'}to) {
                  contributionCalendar {
                    totalContributions
                    weeks {
                      contributionDays {
                        date
                        contributionCount
                      }
                    }
                  }
                  commitContributionsByRepository(maxRepositories: 100) {
                    contributions { totalCount }
                    repository {
                      nameWithOwner
                      isFork
                      isArchived
                      primaryLanguage { name }
                      languages(first: 20, orderBy: { field: SIZE, direction: DESC }) {
                        edges {
                          size
                          node { name }
                        }
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }
}

private suspend fun HttpResponse.requireSuccess(): HttpResponse {
    if (!status.isSuccess()) {
        val body = runCatching { body<String>() }.getOrDefault("<unreadable body>")
        error("GitHub API ${status.value} ${status.description}: $body")
    }
    return this
}
