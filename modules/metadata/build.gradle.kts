plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)

    id("multiplatform.target.jvm")
}

kotlin {
    jvm {
        @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
        binaries {
            executable {
                mainClass = "dev.stashy.metadata.MainKt"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)

            implementation(kotlinLibs.serialization.json)
            implementation(kotlinLibs.datetime)
            implementation(kotlinLibs.coroutines.core)

            implementation(libs.clikt)

            implementation(ktorLibs.client.core)
            implementation(ktorLibs.client.cio)
            implementation(ktorLibs.client.contentNegotiation)
            implementation(ktorLibs.client.logging)
            implementation(ktorLibs.serialization.kotlinx.json)

            implementation(libs.slf4j.simple)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }
    }
}
