package multiplatform.target

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import getOrThrow
import getVersionOrThrow
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import toIntOrElseThrow

configure<KotlinMultiplatformExtension> {
    this.extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("androidLibrary") {
        val androidLibs = versionCatalogs.getOrThrow("androidLibs")

        compileSdk = androidLibs.getVersionOrThrow("compileSdk").requiredVersion
            .toIntOrElseThrow { IllegalArgumentException("Invalid compileSdk version $it") }
        minSdk = androidLibs.getVersionOrThrow("minSdk").requiredVersion
            .toIntOrElseThrow { IllegalArgumentException("Invalid compileSdk version $it") }

        compilerOptions {
            val jvmVersion = androidLibs.getVersionOrThrow("jvm").requiredVersion
            jvmTarget = JvmTarget.fromTarget(jvmVersion)
        }
    }
}
