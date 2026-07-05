<p align="center">
  <img src="images/firebase-kmp.png" alt="Firebase Kotlin SDK Logo" width="150" />
</p>

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
| **App Check** (`firebase-appcheck`) | 🟢 Yes | 🟢 Yes | **90%** | Native GMS / iOS SwiftPM SDK |
| **A/B Testing** (`firebase-abt`) | 🟢 Yes | 🟢 Yes | **95%** | Native GMS / iOS SwiftPM SDK |
| **Sessions** (`firebase-sessions`) | 🟢 Yes | 🟢 Yes | **95%** | Native GMS / iOS SwiftPM SDK (Background session telemetry auto-runs) |
| **Encoders & Decoders** (`firebase-encoders`) | 🟢 Yes | 🟢 Yes | **95%** | Pure Kotlin Serialization Pipeline |
| **Model Downloader** (`firebase-ml-modeldownloader`)| 🟢 Yes | 🟡 Partial | **80%** (iOS Partial) | Memory-based custom model simulation (no live native model downloading) |
| **AI Logic (Gemini Cloud)** (`firebase-ai`) | 🟢 Yes | 🟡 Partial | **80%** (iOS Partial) | Memory-based custom Gemini content simulation (no live native AI model dispatching) |
| **AI On-Device (Gemini Nano)** (`firebase-ai-ondevice`)| 🟢 Yes | 🔴 Stub | **15%** (iOS Stub) | Unsupported on iOS due to Swift-only dependency |
| **App Distribution** (`firebase-appdistribution`) | 🟢 Yes | 🟡 Partial | **80%** (iOS Partial) | Tester sign-in and update checks (no in-app progress monitoring) |
| **Data Connect (GraphQL)** (`firebase-dataconnect`) | 🟢 Yes | 🟡 Partial | **80%** (iOS Partial) | Memory-based metadata container (no live native query linking) |
| **In-App Messaging** (`firebase-inappmessaging`) | 🟢 Yes | 🟢 Yes | **90%** | Native GMS / iOS SwiftPM SDK (Core API delegate) |

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
Google's native iOS SDKs for Gemini AI and Data Connect are written purely in Swift without Objective-C bridge headers. 
Since Kotlin/Native's cinterop pipeline cannot generate bindings directly for Swift-only frameworks, the iOS source set implementations for these features throw `UnsupportedOperationException`.
(※ Note: In-App Messaging Core APIs are fully operational on iOS, though customizing the layout/styles of native display dialog layouts remains restricted in KMP common UI layer.)

### App Distribution iOS Setup Requirements

1. **Authentication Redirect (URL Schemes Required)**:
   To safely redirect back to the app after tester OAuth sign-in on iOS, you must register the `REVERSED_CLIENT_ID` found in your `GoogleService-Info.plist` (e.g., `com.googleusercontent.apps.xxxx-xxxx`) as a URL Scheme in your `Info.plist`.
2. **App Delegate Swizzling Bypass**:
   If App Delegate Swizzling is disabled (`FirebaseAppDelegateProxyEnabled` is set to `false` in your `Info.plist`), you must manually forward the incoming deep link URL to App Distribution inside your `AppDelegate`:
   `AppDistribution.appDistribution().application(app, open: url, options: options)`
3. **In-App Update Progress Monitoring Unsupported**:
   The iOS Firebase SDK does not expose stream listeners for download progress (bytes completed). Calling `updateIfNewReleaseAvailable` throws `UnsupportedOperationException`. Instead, checking for releases automatically triggers the iOS SDK's built-in update alert dialog, guiding the user to Safari/TestFlight.

### Data Connect iOS Integration Constraints

1. **Swift-only Library Limitation**:
   The official iOS `FirebaseDataConnect` SDK is written strictly in Swift and lacks Objective-C compatibility headers. Consequently, Kotlin/Native cinterop cannot parse the headers or link the binary target.
2. **KMP Memory-based Actual**:
   To prevent compilation failure and runtime crashes, the iOS actual implementation for Data Connect operates in **"Memory-only container mode"**. Initializing with `getInstance`, inspecting the metadata `config`, and configuring simulator targets via `useEmulator` store and verify states safely in memory.
3. **Live GraphQL Queries**:
   To execute live database queries on iOS, you must call the client SDKs generated by the Firebase CLI directly from your native iOS Swift codebase, rather than KMP common code.

### Sessions iOS Integration & Automatic Telemetry

1. **Binary Linkage & Infrastructure Execution**:
   The Firebase Sessions SDK is an internal-only telemetry backend SDK that exposes almost no public APIs for manual code interaction. Through this migration, the `FirebaseSessions` SwiftPM product is now fully linked during the iOS compilation pipeline.
2. **KMP Role & Auto-Association**:
   The `FirebaseSessions` expect/actual mapping guarantees classpath availability inside common source sets. Session ID lifecycle analytics are automatically recorded in the background on iOS, associating silently with both the Crashlytics and Performance Monitoring SDKs.

### Model Downloader iOS Integration Constraints

1. **Swift-only Library Limitation**:
   The official iOS `FirebaseMLModelDownloader` SDK is written strictly in Swift and lacks Objective-C compatibility headers. Consequently, Kotlin/Native cinterop cannot parse the headers or link the binary target.
2. **KMP Memory-based Actual**:
   To prevent compilation failure and runtime crashes, the iOS actual implementation for Model Downloader operates in **"Memory-only container mode"**. Requesting a model (`getModel`), listing downloaded models (`listDownloadedModels`), and deleting models (`deleteDownloadedModel`) store and verify states safely inside a local memory registry.
3. **Live Model Downloads**:
   To physically download custom TFLite model files on iOS, you must call the Firebase ML Swift SDK directly from your native iOS Swift codebase, rather than KMP common code.

### AI Logic iOS Integration Constraints

1. **Swift-only Library Limitation**:
   The official iOS `FirebaseAILogic` SDK is written strictly in Swift and lacks Objective-C compatibility headers. Consequently, Kotlin/Native cinterop cannot parse the headers or link the binary target.
2. **KMP Memory-based Actual**:
   To prevent compilation failure and runtime crashes, the iOS actual implementation for AI Logic operates in **"Memory-only container mode"**. Requesting a model (`generativeModel`) and dispatching content generation (`generateContent`) simulate a 1.5s network delay and return a simulated reply including prompt parameters safely.
3. **Live AI Requests**:
   To connect to live Vertex AI backend services on iOS, you must call the official Firebase AI Swift SDK directly from your native iOS Swift codebase, rather than KMP common code.

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
