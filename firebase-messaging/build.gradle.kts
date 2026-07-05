@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "zone.ien.firebase.messaging"
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
        iosSimulatorArm64(),
        iosArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FirebaseMessaging"
            isStatic = true
        }
        iosTarget.binaries.all {
            val sdkName = if (iosTarget.name.contains("Simulator")) "iphonesimulator" else "iphoneos"
            val buildTypeName = if (buildType == org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG) "Debug" else "Release"
            val frameworkPath = "${layout.buildDirectory.get().asFile}/kotlin/swiftImportDd/dd_$sdkName/Build/Products/$buildTypeName-$sdkName/PackageFrameworks"
            linkerOpts("-F$frameworkPath")
        }
    }

    // SwiftPM Firebase iOS SDK integration
    swiftPMDependencies {
        discoverClangModulesImplicitly.set(false)
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.ios.sdk.get()),
            products = listOf(product("FirebaseMessaging"))
        )
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":firebase-common"))
            api(project(":firebase-components"))
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.android.bom))
            api(libs.firebase.android.messaging)
            implementation(libs.kotlinx.coroutines.play.services)
        }

        val iosMain by creating {
            dependsOn(commonMain.get())
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}