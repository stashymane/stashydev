plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)
    alias(androidLibs.plugins.library)

    id("multiplatform.target.jvmDesktop")
    id("multiplatform.target.wasmJs")
    id("multiplatform.target.androidLibrary")
}

kotlin {
    android {
        namespace = "dev.stashy.home"

        withJava()
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)
            implementation(projects.modules.icons)
            implementation(projects.modules.shaders)
            implementation(projects.modules.navigation)
            implementation(projects.modules.data)

            implementation(kotlinLibs.serialization.json)

            implementation(composeLibs.bundles.jb)
            implementation(composeLibs.bundles.lifecycle)
            implementation(composeLibs.bundles.adaptive)
            implementation(composeLibs.bundles.nav3)
            implementation(composeLibs.bundles.app)
            implementation(composeLibs.koalaplot)

            implementation(libs.bundles.koin)
            implementation(libs.colormath.core)
            implementation(libs.colormath.compose)

            implementation(ktorLibs.client.core)
            implementation(ktorLibs.client.contentNegotiation)
            implementation(ktorLibs.client.resources)
            implementation(ktorLibs.client.logging)
            implementation(ktorLibs.client.serialization)
            implementation(ktorLibs.serialization.kotlinx.json)
            implementation(ktorLibs.resources)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }

        desktopMain.dependencies {
            implementation(kotlinLibs.coroutines.swing)

            implementation(ktorLibs.client.cio)
            implementation(libs.slf4j.simple)
        }

        wasmJsMain.dependencies {
            implementation(kotlinLibs.browser)
        }
    }
}

dependencies {
    androidRuntimeClasspath(composeLibs.jb.uiTooling)
}

compose {
    resources {
        packageOfResClass = "dev.stashy.home"
    }
}
