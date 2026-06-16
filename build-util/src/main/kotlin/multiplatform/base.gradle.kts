package multiplatform

import getOrThrow
import getVersionOrThrow
import moduleName
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import toIntOrElseThrow

configure<BasePluginExtension> {
    archivesName.convention(provider(project::moduleName))
}

configure<KotlinMultiplatformExtension> {
    val jvmVersion = versionCatalogs.getOrThrow("libs")
        .getVersionOrThrow("jvm")
        .requiredVersion
        .toIntOrElseThrow { IllegalArgumentException("Invalid \"jvm\" version $it") }

    jvmToolchain(jvmVersion)

    compilerOptions {
        optIn.addAll("kotlin.time.ExperimentalTime", "kotlin.uuid.ExperimentalUuidApi")
        freeCompilerArgs.addAll(
            "-Xreturn-value-checker=check",
            "-Xcontext-sensitive-resolution",
//            "-XXLanguage:+JvmInlineMultiFieldValueClasses"
        )
    }
}
