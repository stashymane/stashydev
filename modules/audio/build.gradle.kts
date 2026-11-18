plugins {
    alias(kotlinLibs.plugins.multiplatform)

    id("multiplatform.target.jvm")
    id("multiplatform.target.js")
    id("multiplatform.target.wasmJs")
}

kotlin {
    explicitApi()

    compilerOptions {
        optIn.addAll("kotlin.ExperimentalStdlibApi", "kotlin.ExperimentalWasmJsInterop")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlinLibs.coroutines.core)
        }

        webMain.dependencies {
            implementation(kotlinLibs.browser)
        }

        jvmMain.dependencies {
            implementation(libs.jlayer)
            implementation(libs.mp3spi)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
            implementation(kotlinLibs.coroutines.test)
        }
    }
}
