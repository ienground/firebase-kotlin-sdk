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
include(":firebase-components:firebase-dynamic-module-support")
include(":firebase-annotations")
include(":firebase-abt")
include(":firebase-storage")
include(":firebase-database")
include(":firebase-database-collection")
include(":firebase-crashlytics")
include(":firebase-functions")
include(":example")
include(":example:androidApp")
include(":example:composeApp")
include(":example:iosApp")
