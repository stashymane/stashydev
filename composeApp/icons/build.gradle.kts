plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)

    id("multiplatform.target.jvmDesktop")
    id("multiplatform.target.wasmJs")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(composeLibs.jb.runtime)
            implementation(composeLibs.jb.foundation)
            implementation(composeLibs.jb.ui)
            implementation(composeLibs.jb.uiToolingPreview)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(kotlinLibs.coroutines.swing)
        }
    }
}
