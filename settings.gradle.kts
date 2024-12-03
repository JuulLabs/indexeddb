rootProject.name = "indexeddb"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    "core",
    "external",
    "logging-khronicle",
)
