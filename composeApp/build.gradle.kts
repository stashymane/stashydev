@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.composeCompiler)
    alias(composeLibs.plugins.multiplatform)
    alias(composeLibs.plugins.hotReload)
}

kotlin {
    jvm()

    wasmJs {
        outputModuleName = "composeApp"

        browser()
        binaries.executable()

        compilerOptions {
            freeCompilerArgs.addAll("-Xwasm-debugger-custom-formatters")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)
            implementation(projects.modules.githubApi)
            implementation(libs.kotlinx.serialization.json)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.bundles.koin)
            implementation(composeLibs.bundles.coil)
            implementation(composeLibs.navigation)
            implementation(composeLibs.materialKolor)

            implementation(ktorLibs.bundles.client)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs) {
                exclude(compose.material)
            }
            implementation(libs.kotlinx.coroutines.swing)
            implementation(ktorLibs.client.cio)
            implementation(libs.slf4j.simple)
        }

        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}

compose {
    resources {
        packageOfResClass = "dev.stashy.home"
    }

    desktop {
        application {
            mainClass = "MainKt"
        }
    }
}
