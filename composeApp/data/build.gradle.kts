plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
    alias(kotlinLibs.plugins.atomicfu)
    alias(androidLibs.plugins.library)

    id("multiplatform.target.jvmDesktop")
    id("multiplatform.target.wasmJs")
    id("multiplatform.target.androidLibrary")
}

kotlin {
    android {
        namespace = "dev.stashy.data"
    }

    sourceSets {
        commonMain.dependencies {
            api(kotlinLibs.coroutines.core)
            api(kotlinLibs.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
            implementation(kotlinLibs.coroutines.core)
        }
    }
}
