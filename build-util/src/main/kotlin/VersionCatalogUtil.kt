import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint

internal fun VersionCatalogsExtension.getOrThrow(alias: String): VersionCatalog = find(alias)
    .orElseThrow { IllegalArgumentException("Version catalog named \"$alias\" is not present") }

internal fun VersionCatalog.getVersionOrThrow(alias: String): VersionConstraint =
    findVersion(alias).orElseThrow { IllegalArgumentException("\"$alias\" version was not found in catalog") }

internal fun VersionCatalog.getIntVersionOrThrow(alias: String): Int =
    getVersionOrThrow(alias).requiredVersion.toIntOrElseThrow {
        IllegalArgumentException("Failed to parse version \"$alias\" of \"$it\" as integer")
    }
