pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ienground-kmp-library-template"
include(":library")
include(":example")
include(":example:androidApp")
include(":example:composeApp")
include(":example:iosApp")
