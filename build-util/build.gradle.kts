plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(androidLibs.gradlePlugin.library)
}
