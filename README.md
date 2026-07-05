# Firebase Kotlin SDK

**English** | [한국어](README_ko.md)


[![Kotlin](https://img.shields.io/badge/kotlin-2.4.0-blue.svg)](https://kotlinlang.org)
[![Platform](https://img.shields.io/badge/platform-android%20%7C%20ios-lightgrey.svg)](https://kotlinlang.org/docs/multiplatform.html)

A Kotlin Multiplatform (KMP) wrapper around Firebase platform SDKs, designed to expose native Kotlin-first APIs for Android and iOS projects.

---

## Features

- **Kotlin-First Design**: Native support for Coroutines (`suspend` functions) and asynchronous stream processing (`Flow`).
- **Thin & Light Adaption**: Implements clean expect/actual wrappers that delegate directly to official Firebase Android (GMS/Maven) and iOS (Apple SwiftPM) SDKs.
- **Platform Integrity**: Unsupported iOS platform features gracefully fallback with descriptive stubs rather than crashing on build time.

### Supported Services Matrix

| Firebase Feature | Android Support | iOS Support | Completion Rate | Under the Hood |
| :--- | :---: | :---: | :---: | :--- |
| **Authentication** (`firebase-auth`) | 🟢 Yes | 🟢 Yes | **95%** | Native GMS / iOS SwiftPM SDK |
| **Cloud Firestore** (`firebase-firestore`) | 🟢 Yes | 🟢 Yes | **90%** | Native GMS / iOS SwiftPM SDK |
| **Realtime Database** (`firebase-database`) | 🟢 Yes | 🟢 Yes | **85%** | Native GMS / iOS SwiftPM SDK |
| **Cloud Storage** (`firebase-storage`) | 🟢 Yes | 🟢 Yes | **90%** | Native GMS / iOS SwiftPM SDK |
| **Cloud Functions** (`firebase-functions`) | 🟢 Yes | 🟢 Yes | **95%** | Native GMS / iOS SwiftPM SDK |
| **Remote Config** (`firebase-config`) | 🟢 Yes | 🟢 Yes | **90%** | Native GMS / iOS SwiftPM SDK |
| **Crashlytics** (`firebase-crashlytics`) | 🟢 Yes | 🟢 Yes | **90%** | Native GMS / iOS SwiftPM SDK |
| **Cloud Messaging** (`firebase-messaging`) | 🟢 Yes | 🟢 Yes | **85%** | Native GMS / iOS SwiftPM SDK |
| **Performance Monitoring** (`firebase-perf`) | 🟢 Yes | 🟢 Yes | **80%** | Native GMS / iOS SwiftPM SDK |
| **Installations** (`firebase-installations`) | 🟢 Yes | 🟢 Yes | **95%** | Native GMS / iOS SwiftPM SDK |
| **Model Downloader** (`firebase-ml-modeldownloader`)| 🟢 Yes | 🔴 Stub | **10%** (iOS Stub) | Unsupported on iOS due to Swift-only dependency |
| **AI Logic (Gemini Cloud)** (`firebase-ai`) | 🟢 Yes | 🔴 Stub | **15%** (iOS Stub) | Unsupported on iOS due to Swift-only dependency |
| **AI On-Device (Gemini Nano)** (`firebase-ai-ondevice`)| 🟢 Yes | 🔴 Stub | **15%** (iOS Stub) | Unsupported on iOS due to Swift-only dependency |
| **App Distribution** (`firebase-appdistribution`) | 🟢 Yes | 🔴 Stub | **20%** (iOS Stub) | Platform limitation on iOS |
| **Data Connect (GraphQL)** (`firebase-dataconnect`) | 🟢 Yes | 🔴 Stub | **10%** (iOS Stub) | Unsupported on iOS due to Swift-only dependency |
| **In-App Messaging** (`firebase-inappmessaging`) | 🟢 Yes | 🔴 Stub | **10%** (iOS Stub) | Unsupported on iOS due to Swift-only dependency |

---

## Installation

Add the core dependency to your shared Kotlin Multiplatform module's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Core Common Firebase APIs
            implementation("zone.ien.firebase:firebase-common:0.9.0")
            
            // Add required feature wrappers
            implementation("zone.ien.firebase:firebase-auth:0.9.0")
            implementation("zone.ien.firebase:firebase-firestore:0.9.0")
        }
    }
}
```

### Platform-Specific Setup

#### Android
Ensure your root `build.gradle.kts` applies the Google Services plugin, and your app-level module imports the configuration file (`google-services.json`).

#### iOS (SwiftPM Integration)
This library uses native Swift Package Manager linkage for Apple builds. Make sure the native Xcode package dependencies are resolved during the build pipeline:
```bash
# Resolve SPM dependencies under the iOS build step
xcodebuild -resolvePackageDependencies -workspace iosApp.xcworkspace -scheme iosApp
```

> [!IMPORTANT]
> **Minimum Kotlin Version**: This SwiftPM import feature relies on the official Swift Package Manager integration introduced in **Kotlin 2.4.0** (utilizing the new native `swiftPMDependencies` DSL). Therefore, **Kotlin 2.4.0 or higher is strictly required** as the minimum compiler version to compile and link this library's iOS targets.


---

## Usage Example

### Core Initialization
Initialize the Firebase SDK using your platform context:

```kotlin
import zone.ien.firebase.Firebase
import zone.ien.firebase.initialize

// Initialize Firebase Core
val app = Firebase.initialize(context) // FirebasePlatformContext
```

### Firestore Query
Interact with Firestore collection documents natively:

```kotlin
import zone.ien.firebase.firestore.firestore
import zone.ien.firebase.Firebase

val db = Firebase.firestore
val document = db.collection("users").document("user_id")

// Set Data asynchronously
document.set(mapOf("name" to "John Doe", "age" to 30))

// Get Data natively via Kotlin Coroutines
val snapshot = document.get()
val userName = snapshot.get<String>("name")
```

---

## Migration Guide

### Who Needs to Migrate?
1. Developers transitionally upgrading from native **GMS Android SDKs** to Kotlin Multiplatform.
2. Teams migrating from alternative KMP wrappers (e.g. **GitLive's firebase-kotlin-sdk**) seeking explicit stub fallbacks for unsupported iOS architectures.

### Namespace Mappings

Rename your packaging imports to adapt to this SDK's namespaces:

| Target Component | Upstream Android SDK | GitLive SDK | This SDK Namespace |
| :--- | :--- | :--- | :--- |
| **Core App** | `com.google.firebase.Firebase` | `dev.gitlive.firebase.Firebase` | `zone.ien.firebase.Firebase` |
| **Auth** | `com.google.firebase.auth.FirebaseAuth` | `dev.gitlive.firebase.auth.auth` | `zone.ien.firebase.auth.auth` |
| **Firestore** | `com.google.firebase.firestore.FirebaseFirestore`| `dev.gitlive.firebase.firestore.firestore`| `zone.ien.firebase.firestore.firestore` |
| **Remote Config** | `com.google.firebase.remoteconfig.FirebaseRemoteConfig`| `dev.gitlive.firebase.config.config`| `zone.ien.firebase.config.config` |

### Key API Behavior Changes

- **Synchronous vs Asynchronous Task Mappings**: Android-specific `Task<T>` and callback models are mapped to standard Kotlin `suspend` functions returning `T` directly.
- **Real-time Event Observers**: Event listeners are exposed as pure Kotlin `Flow<T>` streams. Replace older callback attachments with `.collect { ... }` blocks inside your Coroutine lifecycle.
- **iOS Unsupported Stubs**: Features stubbed on iOS (such as `firebase-inappmessaging` or `firebase-dataconnect`) throw `UnsupportedOperationException` at runtime instead of failing compilation, permitting shared common code declarations.

---

## Platform Limitations & Breaking Changes

### Swift-only cinterop compilation constraints (iOS)
Google's native iOS SDKs for In-App Messaging, Gemini AI, and Data Connect are written purely in Swift without Objective-C bridge headers. 
Since Kotlin/Native's cinterop pipeline cannot generate bindings directly for Swift-only frameworks, the iOS source set implementations for these features throw `UnsupportedOperationException`.

**Action Needed**: Guard your UI entry points or calls to these services on iOS:
```kotlin
import zone.ien.firebase.example.util.isIos

if (!isIos) {
    // Safely run Android-supported AI logic
    Firebase.ai.generativeModel("gemini-3.5-flash").generateContent(prompt)
} else {
    // Display unsupported fallback notice
}
```

---

## License

```
Copyright 2026 Firebase Kotlin SDK Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
