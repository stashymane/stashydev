plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)

    id("multiplatform.target.jvmDesktop")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.common)
            implementation(projects.modules.icons)

            implementation(composeLibs.bundles.jb)
            implementation(kotlinLibs.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
