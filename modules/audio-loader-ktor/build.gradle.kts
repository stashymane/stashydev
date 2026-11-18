plugins {
    alias(kotlinLibs.plugins.multiplatform)

    id("multiplatform.target.jvm")
    id("multiplatform.target.js")
    id("multiplatform.target.wasmJs")
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            api(projects.modules.audio)

            api(ktorLibs.client.core)
        }

        jvmMain.dependencies {
            implementation(ktorLibs.client.cio)
        }

        webMain.dependencies {
            implementation(ktorLibs.client.js)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }
    }
}
