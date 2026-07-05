plugins {
    kotlin("jvm")
    application
}

group = "zone.ien.firebase"
version = libs.versions.lib.version.name.get()

application {
    mainClass.set("zone.ien.firebase.encoders.protoc.ProtocGenFirebaseEncodersKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    // Explicit version is required since protobuf-java is not part of Firebase BoM
    implementation("com.google.protobuf:protobuf-java:3.25.1")

    testImplementation(libs.kotlin.test)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
