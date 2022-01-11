buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.dokka)
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("gh-pages"))
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
