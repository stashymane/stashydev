package model

import kotlinx.serialization.Serializable

@Serializable
data class HomeLink(val icon: String, val name: String, val subtitle: String? = null, val url: String)
