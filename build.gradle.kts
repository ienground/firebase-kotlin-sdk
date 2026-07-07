plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

subprojects {
    plugins.withId("com.vanniktech.maven.publish") {
        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            publishToMavenCentral(automaticRelease = true)

            // Artifact ID만 각 프로젝트의 이름으로 자동 설정
            group = "zone.ien.firebase"
            version = libs.versions.lib.version.name.get()
            println("${group} ${project.name} ${version}")

            coordinates(group.toString(), project.name, version.toString())

            pom {
                name = project.name
                description = "About Firebase Kotlin Multiplatform SDK"
                inceptionYear = "2026"
                url = "https://github.com/ienground/firebase-kotlin-sdk"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        id = "ienground"
                        name = "Ericano Rhee"
                        url = "my@ien.zone"
                    }
                }
                scm {
                    url = "https://github.com/ienground/firebase-kotlin-sdk.git"
                    connection = "scm:git:https://github.com/ienground/firebase-kotlin-sdk.git"
                    developerConnection = "scm:git:https://github.com/ienground/firebase-kotlin-sdk.git"
                }
            }

            val isPublishingToMavenLocal = gradle.startParameter.taskNames.any { it.contains("publishToMavenLocal", ignoreCase = true) }
            val isSnapshot = version.toString().endsWith("SNAPSHOT")

            if (!isSnapshot && !isPublishingToMavenLocal) {
                signAllPublications()
            }
        }
    }
}