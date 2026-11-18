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

dokka {
    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("gh-pages"))
    }
}

dependencies {
    dokka(project(":core"))
    dokka(project(":external"))
    dokka(project(":logging-khronicle"))
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
