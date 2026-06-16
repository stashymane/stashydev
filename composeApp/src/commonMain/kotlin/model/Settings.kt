package model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val lightMode: Boolean = false,
)
