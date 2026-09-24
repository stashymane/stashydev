import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

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
            group("skikoCommon") {
                withJvm()
                withWasmJs()
            }
        }
    }

    android {
        namespace = "dev.stashy.tiled"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(composeLibs.jb.ui)
            implementation(composeLibs.coil.main)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }

        wasmJsMain.dependencies {
            implementation(kotlinLibs.browser)
        }
    }
}
