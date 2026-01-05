plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(composeLibs.plugins.compose) apply false
    alias(composeLibs.plugins.hotReload) apply false
}
