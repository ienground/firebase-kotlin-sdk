import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "zone.ien.firebase"
version = libs.versions.lib.version.name.get()

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "zone.ien.firebase.appcheck.debug"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava()
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FirebaseAppCheckDebug"
            isStatic = true
        }
    }

    // Kotlin 2.4.0 SwiftPM Integration Configuration
    swiftPMDependencies {
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.ios.sdk.get()),
            products = listOf(product("FirebaseAppCheck"))
        )
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":firebase-common"))
            implementation(project(":firebase-components"))
            implementation(project(":appcheck:firebase-appcheck"))
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.android.bom))
            api(libs.firebase.android.appcheck.debug)
        }

        val androidMain by getting {
            kotlin.setSrcDirs(listOf("src/androidMain/kotlin"))
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(group.toString(), "firebase-appcheck-debug", version.toString())

    pom {
        name = "Firebase AppCheck Debug KMP"
        description = "Kotlin Multiplatform Firebase AppCheck Debug Provider Factory Wrapper"
        inceptionYear = "2026"
        url = "https://github.com/ienground/firebase-kotlin-sdk"
        licenses {
            license {
                name = "The Apache Software License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "ienground"
                name = "IEN Ground"
            }
        }
        scm {
            url = "https://github.com/ienground/firebase-kotlin-sdk.git"
            connection = "scm:git:https://github.com/ienground/firebase-kotlin-sdk.git"
            developerConnection = "scm:git:https://github.com/ienground/firebase-kotlin-sdk.git"
        }
    }
}
