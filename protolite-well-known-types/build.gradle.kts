plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "zone.ien.firebase.protolite"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                // Common dependencies if any
            }
        }
        androidMain {
            dependencies {
                api(libs.firebase.android.protolite.well.known.types)
            }
        }
        iosMain {
            dependencies {
                // iOS stubs do not require native library dependency
            }
        }
    }
}
