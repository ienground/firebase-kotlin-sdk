val localPropertiesFile = settingsDir.resolve("local.properties")
if (localPropertiesFile.exists()) {
    val localProperties = java.util.Properties()
    localPropertiesFile.inputStream().use { localProperties.load(it) }
    gradle.beforeProject {
        localProperties.forEach { key, value ->
            extra.set(key as String, value)
        }
    }
}

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
include(":ai-logic:firebase-ai")
include(":ai-logic:firebase-ai-ondevice")
include(":ai-logic:firebase-ai-ondevice-interop")
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
include(":firebase-appdistribution")
include(":firebase-appdistribution-api")
include(":firebase-dataconnect")
include(":firebase-dataconnect:connectors")
include(":firebase-inappmessaging")
include(":firebase-inappmessaging-display")
include(":encoders:firebase-encoders")
include(":transport:transport-api")
include(":transport:transport-backend-cct")
include(":transport:transport-runtime")
include(":transport:transport-runtime-testing")
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

project(":encoders:firebase-encoders").projectDir = file("encoders/firebase-encoders")

include(":encoders:firebase-encoders-json")
project(":encoders:firebase-encoders-json").projectDir = file("encoders/firebase-encoders-json")

include(":encoders:firebase-encoders-processor")
project(":encoders:firebase-encoders-processor").projectDir = file("encoders/firebase-encoders-processor")

include(":encoders:firebase-encoders-proto")
project(":encoders:firebase-encoders-proto").projectDir = file("encoders/firebase-encoders-proto")

include(":encoders:firebase-encoders-reflective")
project(":encoders:firebase-encoders-reflective").projectDir = file("encoders/firebase-encoders-reflective")

include(":encoders:firebase-decoders-json")
project(":encoders:firebase-decoders-json").projectDir = file("encoders/firebase-decoders-json")

include(":encoders:protoc-gen-firebase-encoders")
project(":encoders:protoc-gen-firebase-encoders").projectDir = file("encoders/protoc-gen-firebase-encoders")

include(":firebase-messaging")
project(":firebase-messaging").projectDir = file("firebase-messaging")

include(":firebase-messaging-directboot")
project(":firebase-messaging-directboot").projectDir = file("firebase-messaging-directboot")
