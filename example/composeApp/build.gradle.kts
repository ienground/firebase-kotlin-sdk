import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
            implementation(project(":firebase-database"))
            implementation(project(":firebase-database-collection"))
            implementation(project(":firebase-crashlytics"))
            implementation(project(":firebase-crashlytics-ndk"))
            implementation(project(":firebase-auth"))
            implementation(project(":firebase-functions"))
            implementation(project(":firebase-datatransport"))
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
