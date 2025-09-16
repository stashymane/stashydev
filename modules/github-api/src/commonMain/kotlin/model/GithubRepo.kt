package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class GithubRepo @OptIn(ExperimentalTime::class) constructor(
    val id: Int,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val owner: GithubUser,
    val url: String,
    val description: String? = null,
    val language: String? = null,
    @SerialName("forks_count") val forks: Int,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("watchers_count") val watchers: Int,
    /** Size of repo in kilobytes */
    val size: Int,
    val topics: List<String> = listOf(),
    val license: License? = null,
    @SerialName("pushed_at") val pushedAt: Instant? = null,
    @SerialName("created_at") val createdAt: Instant? = null,
    @SerialName("updated_at") val updatedAt: Instant? = null,
) {
    @Serializable
    data class License(
        val key: String,
        val name: String,
        @SerialName("spdx_id") val spdxId: String? = null,
        val url: String? = null,
        @SerialName("node_id") val nodeId: String
    )

    @Serializable
    enum class OwnerType {
        @SerialName("all")
        ALL,

        @SerialName("owner")
        OWNER,

        @SerialName("member")
        MEMBER
    }

    @Serializable
    enum class SortBy {
        @SerialName("created")
        CREATED,

        @SerialName("updated")
        UPDATED,

        @SerialName("pushed")
        PUSHED,

        @SerialName("full_name")
        FULL_NAME
    }

    @Serializable
    enum class Direction {
        @SerialName("asc")
        ASCENDING,

        @SerialName("desc")
        DESCENDING
    }
}
