plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)

    id("multiplatform.target.jvm")
    id("multiplatform.target.wasmJs")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(kotlinLibs.datetime)
            api(ktorLibs.http)

            implementation(kotlinLibs.serialization.json)
            implementation(kotlinLibs.io)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }
    }
}
