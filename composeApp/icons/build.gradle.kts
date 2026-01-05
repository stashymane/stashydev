@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(composeLibs.jb.runtime)
            implementation(composeLibs.jb.foundation)
            implementation(composeLibs.jb.ui)
            implementation(composeLibs.jb.uiToolingPreview)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(kotlinLibs.coroutines.swing)
        }
    }
}
