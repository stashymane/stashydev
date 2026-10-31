package dev.stashy.metadata.github.api.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class GraphqlResponse<T>(
    val data: T? = null,
    val errors: List<GraphqlError>? = null,
)

@Serializable
internal data class GraphqlError(val message: String)

@Serializable
internal data class CountConnection(val totalCount: Int)

@Serializable
internal data class LanguageName(val name: String)

@Serializable
internal data class LanguageConnection(
    val edges: List<LanguageEdge>,
)

@Serializable
internal data class LanguageEdge(
    val size: Long,
    val node: LanguageName,
)
