@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
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
        namespace = "zone.ien.firebase.encoders.reflective"
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

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FirebaseEncodersReflective"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":encoders:firebase-encoders"))
        }

        jvmMain.dependencies {
            implementation(kotlin("reflect"))
        }

        val androidMain by getting {
            dependsOn(jvmMain.get())
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
