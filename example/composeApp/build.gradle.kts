import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.nio.file.Files
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
            val frameworkPath = "${layout.buildDirectory.get().asFile}/kotlin/swiftImportDd/dd_$sdkName/Build/Products/Debug-$sdkName/PackageFrameworks"
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
                product("FirebaseAppCheck")
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

            implementation(libs.bundles.ienlab.cmp)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

tasks.register("createFirebaseFrameworkSymlinks") {
    doLast {
        val sdkNames = listOf("iphonesimulator", "iphoneos")
        val frameworks = listOf(
            "FirebaseCore", "FirebaseFirestore", "FirebaseAuth", "FirebaseCrashlytics",
            "FirebaseDatabase", "FirebaseStorage", "FirebaseFunctions", "FirebaseAppCheck",
            "FirebaseAppCheckInterop", "FirebaseCoreExtension"
        )
        
        sdkNames.forEach { sdkName ->
            val packageFrameworksPath = file("${layout.buildDirectory.get().asFile}/kotlin/swiftImportDd/dd_$sdkName/Build/Products/Debug-$sdkName/PackageFrameworks")
            if (packageFrameworksPath.exists()) {
                val dylibPath = "KotlinMultiplatformLinkedPackageDylib.framework/KotlinMultiplatformLinkedPackageDylib"
                frameworks.forEach { frameworkName ->
                    val fwDir = file("$packageFrameworksPath/$frameworkName.framework")
                    if (!fwDir.exists()) {
                        fwDir.mkdirs()
                    }
                    val symlinkFile = file("$fwDir/$frameworkName")
                    if (!symlinkFile.exists()) {
                        try {
                            Files.createSymbolicLink(
                                symlinkFile.toPath(),
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

tasks.configureEach {
    if (name.startsWith("linkDebug") || name.startsWith("linkRelease")) {
        dependsOn("createFirebaseFrameworkSymlinks")
    }
}
