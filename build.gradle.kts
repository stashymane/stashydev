plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(composeLibs.plugins.multiplatform) apply false
    alias(composeLibs.plugins.hotReload) apply false
}
