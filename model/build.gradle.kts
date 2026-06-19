plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)

    id("multiplatform.target.common")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(kotlinLibs.serialization.json)
            api(kotlinLibs.datetime)
            api(ktorLibs.http)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }
    }
}
