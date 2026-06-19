plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(buildLibs.kmp.gradlePlugin)
    compileOnly(buildLibs.android.library.gradlePlugin)
}
