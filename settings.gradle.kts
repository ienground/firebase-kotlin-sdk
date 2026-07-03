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
include(":firebase-perf")
include(":firebase-installations")
include(":firebase-installations-interop")
include(":firebase-config-interop")
include(":firebase-ml-modeldownloader")
include(":firebase-config")
include(":firebase-abt")
include(":firebase-storage")
include(":firebase-sessions")
include(":firebase-database")
include(":firebase-database-collection")
include(":firebase-crashlytics")
include(":firebase-crashlytics-ndk")
include(":firebase-auth")
include(":firebase-datatransport")
include(":protolite-well-known-types")
include(":appcheck:firebase-appcheck")
include(":appcheck:firebase-appcheck-interop")
include(":appcheck:firebase-appcheck-debug")
include(":appcheck:firebase-appcheck-playintegrity")
include(":appcheck:firebase-appcheck-recaptcha")
include(":appcheck:firebase-appcheck-debug-testing")
include(":firebase-functions")
include(":example")
include(":example:androidApp")
include(":example:composeApp")
include(":example:iosApp")
