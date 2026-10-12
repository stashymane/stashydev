rootProject.name = "stashydev"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://redirector.kotlinlang.org/maven/compose-dev")
    }
    includeBuild("conventions")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://redirector.kotlinlang.org/maven/compose-dev")
        maven {
            url = uri("https://maven.pkg.github.com/stashymane/standard")
            credentials {
                username = providers.gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                    .getOrElse("")
                password = providers.gradleProperty("gpr.key")
                    .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                    .getOrElse("")
            }
            content {
                includeGroup("dev.stashy.standard")
            }
        }
    }

    versionCatalogs {
        register("androidLibs") {
            from(files("gradle/android.versions.toml"))
        }
        register("kotlinLibs") {
            from(files("gradle/kotlin.versions.toml"))
        }
        register("ktorLibs") {
            from("io.ktor:ktor-version-catalog:3.6.0")
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
    "modules:metadata",
    "app:common",
    "app:desktop",
    "app:web",
    "modules:icons",
    "modules:shaders",
    "modules:tiled",
)
