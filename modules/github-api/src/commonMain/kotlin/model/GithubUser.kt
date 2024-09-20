package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUser(
    val id: Int,
    val login: String,
    val url: String,
    val type: String,
    @SerialName("node_id") val nodeId: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("gravatar_id") val gravatarId: String? = null,
)