@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())

    jvm()

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlinLibs.serialization.json)
            api(kotlinLibs.datetime)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }
    }
}
