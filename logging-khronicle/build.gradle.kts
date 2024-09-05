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

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(libs.khronicle.core)
            }
        }
    }
}
