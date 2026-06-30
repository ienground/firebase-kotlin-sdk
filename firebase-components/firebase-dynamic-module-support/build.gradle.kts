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
        namespace = "com.google.firebase.dynamicloading"
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
            baseName = "FirebaseDynamicModuleSupport"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":firebase-components"))
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.android.bom))
            implementation(libs.firebase.android.dynamic.module.support)
        }

        val androidMain by getting {

        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(group.toString(), "firebase-dynamic-module-support", version.toString())

    pom {
        name = "Firebase Dynamic Feature Modules Support KMP"
        description = "Kotlin Multiplatform Firebase Dynamic Module Loading DI Wrapper"
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
