plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
    alias(kotlinLibs.plugins.atomicfu)

    id("multiplatform.target.jvmDesktop")
    id("multiplatform.target.wasmJs")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(kotlinLibs.coroutines.core)
            implementation(kotlinLibs.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
            implementation(kotlinLibs.coroutines.core)
        }
    }
}
