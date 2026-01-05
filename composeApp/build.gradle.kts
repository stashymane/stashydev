@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.composeCompiler)
    alias(composeLibs.plugins.hotReload)
    alias(composeLibs.plugins.compose)
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
            implementation(projects.composeApp.icons)

            implementation(libs.kotlinx.serialization.json)

            implementation(composeLibs.bundles.jb)
            implementation(composeLibs.bundles.lifecycle)
            implementation(composeLibs.bundles.adaptive)
            implementation(composeLibs.bundles.nav3)
            implementation(composeLibs.bundles.app)

            implementation(libs.bundles.koin)

            implementation(ktorLibs.bundles.client)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(composeLibs.jb.uiTooling)
            implementation(compose.desktop.currentOs)
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
