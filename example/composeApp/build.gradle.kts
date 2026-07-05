@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Paths

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "zone.ien.firebase.example.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources.enable = true

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
        iosTarget.binaries.all {
            val sdkName = if (iosTarget.name.contains("Simulator")) "iphonesimulator" else "iphoneos"
            val buildTypeName = if (buildType == org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG) "Debug" else "Release"
            val frameworkPath = "${layout.buildDirectory.get().asFile}/kotlin/swiftImportDd/dd_$sdkName/Build/Products/$buildTypeName-$sdkName/PackageFrameworks"
            linkerOpts("-F$frameworkPath")
        }
    }
    
    swiftPMDependencies {
        discoverClangModulesImplicitly.set(false)
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.ios.sdk.get()),
            products = listOf(
                product("FirebaseCore"),
                product("FirebaseFirestore"),
                product("FirebaseAuth"),
                product("FirebaseCrashlytics"),
                product("FirebaseDatabase"),
                product("FirebaseStorage"),
                product("FirebaseFunctions"),
                product("FirebaseAppCheck"),
                product("FirebaseRemoteConfig"),
                product("FirebaseMessaging")
            )
        )
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.compose.preview)
            implementation(libs.compose.resources)
            implementation(libs.compose.navigation3)

            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.runtime)

            implementation(project(":firebase-common"))
            implementation(project(":firebase-firestore"))
            implementation(project(":firebase-annotations"))
            implementation(project(":firebase-abt"))
            implementation(project(":firebase-storage"))
            implementation(project(":firebase-sessions"))
            implementation(project(":firebase-perf"))
            implementation(project(":firebase-installations"))
            implementation(project(":firebase-database"))
            implementation(project(":firebase-database-collection"))
            implementation(project(":firebase-crashlytics"))
            implementation(project(":firebase-crashlytics-ndk"))
            implementation(project(":firebase-auth"))
            implementation(project(":firebase-functions"))
            implementation(project(":firebase-datatransport"))
            implementation(project(":protolite-well-known-types"))
            implementation(project(":appcheck:firebase-appcheck"))
            implementation(project(":appcheck:firebase-appcheck-interop"))
            implementation(project(":appcheck:firebase-appcheck-debug"))
            implementation(project(":appcheck:firebase-appcheck-playintegrity"))
            implementation(project(":appcheck:firebase-appcheck-recaptcha"))
            implementation(project(":appcheck:firebase-appcheck-debug-testing"))
            implementation(project(":firebase-ml-modeldownloader"))
            implementation(project(":firebase-config"))
            implementation(project(":ai-logic:firebase-ai"))
            implementation(project(":ai-logic:firebase-ai-ondevice"))
            implementation(project(":transport:transport-api"))
            implementation(project(":transport:transport-backend-cct"))
            implementation(project(":transport:transport-runtime"))
            implementation(project(":firebase-appdistribution"))
            implementation(project(":firebase-dataconnect"))
            implementation(project(":firebase-dataconnect:connectors"))
            implementation(project(":firebase-inappmessaging"))
            implementation(project(":firebase-inappmessaging-display"))
            implementation(project(":encoders:firebase-encoders"))
            implementation(project(":encoders:firebase-encoders-json"))
            implementation(project(":encoders:firebase-encoders-proto"))
            implementation(project(":encoders:firebase-encoders-reflective"))
            implementation(project(":encoders:firebase-decoders-json"))
            implementation(project(":firebase-messaging"))
 
            implementation(libs.bundles.ienlab.cmp)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


tasks.register("createFirebaseFrameworkSymlinks") {
    dependsOn(tasks.matching { it.name.startsWith("cinteropSwiftPMImport") })
    doLast {
        val sdkNames = listOf("iphonesimulator", "iphoneos")
        val frameworks = listOf(
            "FirebaseCore", "FirebaseFirestore", "FirebaseAuth", "FirebaseCrashlytics",
            "FirebaseDatabase", "FirebaseStorage", "FirebaseFunctions", "FirebaseAppCheck",
            "FirebaseAppCheckInterop", "FirebaseCoreExtension", "FirebaseRemoteConfig", "FirebaseMessaging"
        )

        val buildTypes = listOf("Debug", "Release")
        sdkNames.forEach { sdkName ->
            buildTypes.forEach { buildType ->
                val packageFrameworksPath = file("${layout.buildDirectory.get().asFile}/kotlin/swiftImportDd/dd_$sdkName/Build/Products/$buildType-$sdkName/PackageFrameworks")
                if (packageFrameworksPath.exists()) {
                    val dylibPath = "KotlinMultiplatformLinkedPackageDylib.framework/KotlinMultiplatformLinkedPackageDylib"
                    frameworks.forEach { frameworkName ->
                        val fwDir = file("$packageFrameworksPath/$frameworkName.framework")
                        if (!fwDir.exists()) {
                            fwDir.mkdirs()
                        }
                        val symlinkFile = file("$fwDir/$frameworkName")
                        val symlinkPath = symlinkFile.toPath()
                        if (!Files.exists(symlinkPath, LinkOption.NOFOLLOW_LINKS)) {
                            try {
                                Files.createSymbolicLink(
                                    symlinkPath,
                                    Paths.get("../$dylibPath")
                                )
                                println("Created symlink for $frameworkName pointing to $dylibPath")
                            } catch (e: Exception) {
                                println("Failed to create symlink for $frameworkName: ${e.message}")
                            }
                        }
                    }
                }
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
    if (name.startsWith("linkDebug") || name.startsWith("linkRelease")) {
        dependsOn("createFirebaseFrameworkSymlinks")
    }
}