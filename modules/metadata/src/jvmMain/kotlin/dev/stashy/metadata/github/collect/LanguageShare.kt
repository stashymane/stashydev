package dev.stashy.metadata.github.collect

import dev.stashy.metadata.github.GitHubApiConfig
import dev.stashy.metadata.github.api.dto.CommitContributionsByRepository

private class LanguageWeight {
    var bytes: Long = 0
    var weightedBytes: Double = 0.0
}

internal fun buildLanguageShare(
    config: GitHubApiConfig,
    login: String,
    contributions: List<CommitContributionsByRepository>,
): Map<String, Double> {
    val weights = accumulateLanguageWeights(config, login, contributions)
    return normalizeToPercentages(weights)
}

private fun accumulateLanguageWeights(
    config: GitHubApiConfig,
    login: String,
    contributions: List<CommitContributionsByRepository>,
): Map<String, LanguageWeight> {
    val profileRepo = "$login/$login"
    val byLanguage = linkedMapOf<String, LanguageWeight>()

    for (entry in contributions) {
        val repo = entry.repository
        if (repo.nameWithOwner.equals(profileRepo, ignoreCase = true)) continue
        if (!config.includeForks && repo.isFork) continue
        if (!config.includeArchived && repo.isArchived) continue

        val commits = entry.contributions.totalCount
        if (commits <= 0) continue

        val edges = repo.languages?.edges.orEmpty()
        if (edges.isEmpty()) {
            val fallback = repo.primaryLanguage?.name ?: continue
            byLanguage.getOrPut(fallback) { LanguageWeight() }.weightedBytes += commits.toDouble()
            continue
        }

        val totalBytes = edges.sumOf { it.size }.coerceAtLeast(1)
        for (edge in edges) {
            val weight = byLanguage.getOrPut(edge.node.name) { LanguageWeight() }
            weight.bytes += edge.size
            weight.weightedBytes += edge.size.toDouble() / totalBytes * commits
        }
    }

    return byLanguage
}

private fun normalizeToPercentages(weights: Map<String, LanguageWeight>): Map<String, Double> {
    val totalWeight = weights.values.sumOf { it.weightedBytes }.takeIf { it > 0 }
        ?: weights.values.sumOf { it.bytes.toDouble() }.coerceAtLeast(1.0)

    return weights
        .mapValues { (_, weight) ->
            val value = if (weight.weightedBytes > 0) weight.weightedBytes else weight.bytes.toDouble()
            value / totalWeight * 100.0
        }
        .toList()
        .sortedByDescending { it.second }
        .toMap()
}
