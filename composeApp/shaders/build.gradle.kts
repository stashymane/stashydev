@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)
    alias(androidLibs.plugins.library)

    id("multiplatform.target.jvmDesktop")
    id("multiplatform.target.wasmJs")
    id("multiplatform.target.androidLibrary")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("jvmCommon") {
                withAndroidTarget()
                withJvm()
            }
            group("skikoCommon") {
                withJvm()
//                withApple()
//                withJs()
                withWasmJs()
            }
        }
    }

    android {
        namespace = "dev.stashy.home.shaders"
    }

    wasmJs {
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(composeLibs.bundles.jb)
            implementation(composeLibs.bundles.lifecycle)
            implementation(composeLibs.bundles.adaptive)
            implementation(composeLibs.bundles.nav3)
            implementation(composeLibs.bundles.app)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(kotlinLibs.coroutines.swing)
        }

        wasmJsMain.dependencies {
            implementation(kotlinLibs.browser)
        }
    }
}

dependencies {
    androidRuntimeClasspath(composeLibs.jb.uiTooling)
}
