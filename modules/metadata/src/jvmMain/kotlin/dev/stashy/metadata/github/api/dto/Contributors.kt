package dev.stashy.metadata.github.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RestContributor(
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    val contributions: Int,
)

@Serializable
internal data class RestContributorStats(
    val author: RestContributorAuthor? = null,
    val weeks: List<RestContributorWeek> = emptyList(),
)

@Serializable
internal data class RestContributorAuthor(
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("html_url") val htmlUrl: String,
)

@Serializable
internal data class RestContributorWeek(
    val c: Int = 0,
)
