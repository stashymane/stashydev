@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)
    alias(composeLibs.plugins.hotReload)
    alias(androidLibs.plugins.library)

    id("multiplatform.target.jvmDesktop")
    id("multiplatform.target.wasmJs")
    id("multiplatform.target.androidLibrary")
}

kotlin {
    androidLibrary {
        namespace = "dev.stashy.home"

        withJava()
    }

    wasmJs {
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)
            implementation(projects.composeApp.icons)
            implementation(projects.composeApp.shaders)

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

        desktopMain.dependencies {
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

dependencies {
    androidRuntimeClasspath(composeLibs.jb.uiTooling)
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
