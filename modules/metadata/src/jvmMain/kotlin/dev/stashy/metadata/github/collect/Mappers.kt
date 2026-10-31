package dev.stashy.metadata.github.collect

import dev.stashy.metadata.github.api.dto.GqlRepository
import dev.stashy.metadata.github.api.dto.GqlUserProfile
import ProfileMeta
import ReleaseMeta
import RepositoryMeta

internal fun GqlUserProfile.toProfileMeta() = ProfileMeta(
    login = login,
    name = name,
    bio = bio,
    avatarUrl = avatarUrl,
    htmlUrl = url,
    company = company,
    location = location,
    blog = websiteUrl?.takeIf { it.isNotBlank() },
    publicRepos = publicRepositories.totalCount,
    publicGists = publicGists.totalCount,
    followers = followers.totalCount,
    following = following.totalCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

internal fun GqlRepository.toRepositoryMeta() = RepositoryMeta(
    name = name,
    fullName = nameWithOwner,
    description = description,
    htmlUrl = url,
    homepage = homepageUrl?.takeIf { it.isNotBlank() },
    stars = stargazerCount,
    forks = forkCount,
    watchers = watchers.totalCount,
    openIssues = openIssues.totalCount,
    isFork = isFork,
    isArchived = isArchived,
    isPrivate = isPrivate,
    primaryLanguage = primaryLanguage?.name,
    languages = languages?.edges
        ?.takeIf { it.isNotEmpty() }
        ?.let { edges ->
            val total = edges.sumOf { it.size }.coerceAtLeast(1).toDouble()
            edges
                .associate { it.node.name to it.size / total * 100.0 }
                .toList()
                .sortedByDescending { it.second }
                .toMap()
        }
        .orEmpty(),
    topics = repositoryTopics?.nodes?.map { it.topic.name }.orEmpty(),
    license = licenseInfo?.spdxId ?: licenseInfo?.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    pushedAt = pushedAt,
    latestRelease = latestRelease?.let {
        ReleaseMeta(
            tagName = it.tagName,
            name = it.name,
            htmlUrl = it.url,
            publishedAt = it.publishedAt,
            isPrerelease = it.isPrerelease,
            isDraft = it.isDraft,
        )
    },
)
