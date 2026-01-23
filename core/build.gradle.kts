plugins {
    kotlin("multiplatform")
    id("org.jmailen.kotlinter")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()

    js {
        browser()
        binaries.library()
    }

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        browser()
        binaries.library()
    }

    sourceSets {
        all {
            compilerOptions.optIn.add("kotlin.js.ExperimentalWasmJsInterop")
        }

        webMain.dependencies {
            api(libs.coroutines.core)
            api(project(":external"))
        }

        webTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.coroutines.test)
        }
    }
}
