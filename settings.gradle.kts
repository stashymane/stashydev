rootProject.name = "stashydev"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("conventions")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        register("androidLibs") {
            from(files("gradle/android.versions.toml"))
        }
        register("kotlinLibs") {
            from(files("gradle/kotlin.versions.toml"))
        }
        register("ktorLibs") {
            from("io.ktor:ktor-version-catalog:3.5.0")
        }
        register("composeLibs") {
            from(files("gradle/compose.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(
    "model",
    ":composeApp",
    ":composeApp:icons",
    ":composeApp:shaders",
)
