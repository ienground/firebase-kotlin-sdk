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
                product("FirebaseMessaging"),
                product("FirebaseAppDistribution-Beta")
            ),
            importedClangModules = listOf(
                "FirebaseCore",
                "FirebaseFirestoreInternal",
                "FirebaseAuth",
                "FirebaseCrashlytics",
                "FirebaseDatabase",
                "FirebaseStorage",
                "FirebaseFunctions",
                "FirebaseAppCheck",
                "FirebaseRemoteConfig",
                "FirebaseMessaging",
                "FirebaseAppDistribution"
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
            implementation(libs.capsule)
            implementation(libs.hig.adaptive)

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
            implementation(project(":firebase-messaging-directboot"))
 
            implementation(libs.bundles.ienlab.cmp)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


val firebaseFrameworks = listOf(
    "FirebaseCore", "FirebaseFirestore", "FirebaseAuth", "FirebaseCrashlytics",
    "FirebaseDatabase", "FirebaseStorage", "FirebaseFunctions", "FirebaseAppCheck",
    "FirebaseAppCheckInterop", "FirebaseCoreExtension", "FirebaseRemoteConfig", "FirebaseMessaging",
    "FirebaseAppDistribution"
)

val firebaseFrameworkSymlinkTasks = mapOf(
    "IosSimulatorArm64" to ("iphonesimulator" to "Iphonesimulator"),
    "IosArm64" to ("iphoneos" to "Iphoneos")
).mapValues { (targetName, sdk) ->
    tasks.register("createFirebaseFrameworkSymlinks$targetName") {
        dependsOn("convertSyntheticImportProjectIntoDefFile${sdk.second}")
        doLast {
            val linkerArgsFile = file("${layout.buildDirectory.get().asFile}/kotlin/swiftImportLdDump/${sdk.first}/arm64.ld")
            if (!linkerArgsFile.isFile) {
                throw GradleException("SwiftPM linker arguments were not generated: $linkerArgsFile")
            }

            val packageFrameworksPaths = linkerArgsFile.readText()
                .split(';')
                .filter { it.startsWith("-F/") }
                .map { file(it.removePrefix("-F")) }
                .filter { it.name == "PackageFrameworks" }
                .distinctBy { it.absolutePath }
            if (packageFrameworksPaths.isEmpty()) {
                throw GradleException("No SwiftPM PackageFrameworks path found in $linkerArgsFile")
            }

            val dylibPath = "KotlinMultiplatformLinkedPackageDylib.framework/KotlinMultiplatformLinkedPackageDylib"
            packageFrameworksPaths.forEach { packageFrameworksPath ->
                if (!file("$packageFrameworksPath/$dylibPath").isFile) {
                    throw GradleException("SwiftPM linked package dylib was not found under $packageFrameworksPath")
                }

                firebaseFrameworks.forEach { frameworkName ->
                    val frameworkDirectory = file("$packageFrameworksPath/$frameworkName.framework")
                    if (!frameworkDirectory.exists()) {
                        frameworkDirectory.mkdirs()
                    }

                    val symlinkPath = file("$frameworkDirectory/$frameworkName").toPath()
                    if (!Files.exists(symlinkPath, LinkOption.NOFOLLOW_LINKS)) {
                        Files.createSymbolicLink(symlinkPath, Paths.get("../$dylibPath"))
                    }
                }
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
    if (name.startsWith("linkDebug") || name.startsWith("linkRelease")) {
        val targetName = when {
            name.contains("IosSimulatorArm64") -> "IosSimulatorArm64"
            name.contains("IosArm64") -> "IosArm64"
            else -> null
        }
        targetName?.let { dependsOn(firebaseFrameworkSymlinkTasks.getValue(it)) }
    }
}

val generateVersionInfo = tasks.register("generateVersionInfo") {
    val outputDir = file("$projectDir/build/generated/sources/version/commonMain/kotlin")
    inputs.property("version", libs.versions.lib.version.name.get())
    outputs.dir(outputDir)
    
    doLast {
        val versionFile = file("$outputDir/zone/ien/firebase/example/util/Version.kt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText("""
            package zone.ien.firebase.example.util
            
            public val libraryVersion: String = "${libs.versions.lib.version.name.get()}"
        """.trimIndent())
    }
}

kotlin.sourceSets.configureEach {
    if (name == "commonMain") {
        kotlin.srcDirs(generateVersionInfo)
    }
}

// Forcefully delete the package resolved lock file during the configuration phase 
// to ensure Gradle's Task Execution graph evaluates resolveSyntheticPackageDependencies as out-of-date!
tasks.configureEach {
    if (name.startsWith("resolveSyntheticPackageDependencies") || name.startsWith("fetchSyntheticImportProjectPackages")) {
        outputs.upToDateWhen { false }
        doFirst {
            val resolvedLockFile = file("build/kotlin/swiftImport/Package.resolved")
            if (resolvedLockFile.exists()) {
                resolvedLockFile.delete()
                logger.lifecycle("Deleted swiftImport/Package.resolved before executing task: $name")
            }
        }
    }
}
