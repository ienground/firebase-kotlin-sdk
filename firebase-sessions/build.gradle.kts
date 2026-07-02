plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "zone.ien.firebase.sessions"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":firebase-common"))
            }
        }
        androidMain {
            dependencies {
                api(libs.firebase.android.sessions)
            }
        }
        iosMain {
            dependencies {
                // iOS stubs do not require native library dependency
            }
        }
    }
}
