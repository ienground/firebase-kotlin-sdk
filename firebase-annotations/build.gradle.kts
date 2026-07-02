import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "zone.ien.firebase.annotations"
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
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FirebaseAnnotations"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Annotations module has no external dependencies
        }

        val androidMain by getting {
            kotlin.setSrcDirs(listOf("src/androidMain/kotlin"))
        }
    }
}
