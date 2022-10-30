plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        binaries.executable()
        browser()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.coroutines.core)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(project(":core"))
            }
        }
    }
}
