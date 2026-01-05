@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)
    alias(composeLibs.plugins.hotReload)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())

    jvm("desktop")

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

            implementation(kotlinLibs.serialization.json)

            implementation(composeLibs.bundles.jb)
            implementation(composeLibs.bundles.lifecycle)
            implementation(composeLibs.bundles.adaptive)
            implementation(composeLibs.bundles.nav3)
            implementation(composeLibs.bundles.app)

            implementation(libs.bundles.koin)

            implementation(ktorLibs.bundles.client)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }

        jvmMain.dependencies {
            implementation(composeLibs.jb.uiTooling)
            implementation(compose.desktop.currentOs)
            implementation(kotlinLibs.coroutines.swing)

            implementation(ktorLibs.client.cio)
            implementation(libs.slf4j.simple)
        }

        wasmJsMain.dependencies {
            implementation(kotlinLibs.browser)
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
