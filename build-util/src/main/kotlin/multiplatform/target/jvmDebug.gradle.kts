package multiplatform.target

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("multiplatform.target.jvm")
}

configure<KotlinMultiplatformExtension> {
    jvm {
        compilations {
            val main by getting
            val debug by creating {
                associateWith(main)
            }
        }
    }
}

val NamedDomainObjectContainer<KotlinSourceSet>.jvmDebug: KotlinSourceSet
    get() = getByName("jvmDebug")
