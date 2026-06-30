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

rootProject.name = "firebase-kotlin-sdk"
include(":firebase-common")
include(":firebase-firestore")
include(":firebase-components")
include(":example")
include(":example:androidApp")
include(":example:composeApp")
include(":example:iosApp")
