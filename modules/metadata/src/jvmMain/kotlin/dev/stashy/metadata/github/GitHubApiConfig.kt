package dev.stashy.metadata.github

data class GitHubApiConfig(
    val output: String,
    val token: String,
    val username: String,
    val repoLimit: Int,
    val includeForks: Boolean,
    val includeArchived: Boolean,
    val apiUrl: String
)
