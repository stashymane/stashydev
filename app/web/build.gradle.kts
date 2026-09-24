@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)

    id("multiplatform.target.wasmJs")
    id("plugins.webPreload")
}

kotlin {
    wasmJs {
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.common)

            implementation(composeLibs.jb.runtime)
            implementation(composeLibs.jb.ui)
        }
    }
}

webPreload {
    distribution {
        wasm = true
    }

    script("app-web.js")

    fetch("/composeResources/dev.stashy.home/font/Inter-VariableFont_opsz_wght.ttf")
    fetch("/composeResources/dev.stashy.home/font/PlayfairDisplay-VariableFont_wght.ttf")
    fetch("/composeResources/dev.stashy.home/font/Geologica-VariableFont_CRSV,SHRP,slnt,wght.ttf")

    prefetch {
        val base = staticBaseUrl.get()
        fetch("$base/api/user.json")
        fetch("$base/api/repo.json")
    }
}
