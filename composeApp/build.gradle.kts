import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines)

            implementation(projects.modules.githubApi)

            implementation(libs.bundles.ktor.client)
            implementation(libs.bundles.coil)

            implementation(libs.materialKolor)
            implementation(libs.navigation)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)

            implementation(compose.components.resources)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs) {
                exclude(compose.material)
            }

            implementation(libs.ktor.client.cio)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}