@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    wasmJs {
        browser()
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines)
            api(libs.kotlinx.datetime)

            implementation(ktorLibs.resources)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(ktorLibs.client.mock)
        }
    }
}
