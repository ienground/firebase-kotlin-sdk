import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "zone.ien.firebase.abt"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava()
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FirebaseAbt"
            isStatic = true
        }
    }

    // Kotlin 2.4.0 SwiftPM Integration Configuration for A/B Testing ObjC wrapper
    swiftPMDependencies {
        discoverClangModulesImplicitly.set(false)
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.ios.sdk.get()),
            products = listOf(product("FirebaseRemoteConfig")),
            importedClangModules = listOf("FirebaseABTesting")
        )
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":firebase-common"))
            implementation(project(":firebase-components"))
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.android.bom))
            api(libs.firebase.android.abt)
        }

        val androidMain by getting {
            kotlin.setSrcDirs(listOf("src/androidMain/kotlin"))
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
