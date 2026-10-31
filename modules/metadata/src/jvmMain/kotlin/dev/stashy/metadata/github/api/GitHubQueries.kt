package dev.stashy.metadata.github.api

import dev.stashy.metadata.Resources

internal object GitHubQueries {
    val profileQuery: String = load("profile")
    val contributionsQuery: String = load("contributions")

    private fun load(name: String): String =
        Resources.readText("/graphql/$name.graphql")
            ?: error("Missing GraphQL resource 'graphql/$name.graphql'")
}
