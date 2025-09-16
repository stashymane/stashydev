rootProject.name = "stashydev"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("ktorLibs") {
            from(files("gradle/ktor.versions.toml"))
        }
        create("composeLibs") {
            from(files("gradle/compose.versions.toml"))
        }
    }
}

include(
    "model",
    ":composeApp",
    ":modules:github-api"
)
