dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            @Suppress("UnstableApiUsage")
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }

        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        register("androidLibs") {
            from(files("../gradle/android.versions.toml"))
        }
        register("kotlinLibs") {
            from(files("../gradle/kotlin.versions.toml"))
        }
    }
}

rootProject.name = "build-util"

