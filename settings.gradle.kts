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
        maven { //temporary, until ktor fixes wasm compilation https://youtrack.jetbrains.com/issue/KTOR-9681/Update-kotlinx-io-to-0.9.1
            url = uri("https://redirector.kotlinlang.org/maven/ktor-eap")
        }

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
            from("io.ktor:ktor-version-catalog:3.5.1-eap-1639")
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
