# Firebase Android SDK - Kotlin Multiplatform (KMP) Migration Status

This document tracks the KMP migration status across all subprojects defined in the repository.

---

## 📊 Migration Summary

- **Total SDKs**: 35
- **KMP Enabled**: 11
- **Android Native Only**: 24

---

## 📋 Subprojects Migration Matrix

| Subproject Path                                       | Type  | KMP Support | Targets Supported | Notes                    |
|:------------------------------------------------------|:-----:|:-----------:|:-----------------:|:-------------------------|
| `firebase-firestore`                                  | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `appcheck:firebase-appcheck`                          | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `appcheck:firebase-appcheck-debug`                    | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `appcheck:firebase-appcheck-debug-testing`            | `sdk` | 🟢 Migrated |  Android, iOS     | KMP internal support shell. |
| `appcheck:firebase-appcheck-interop`                  | `sdk` | 🟢 Migrated |  Android, iOS     | KMP interop contract wrapper. |
| `appcheck:firebase-appcheck-playintegrity`            | `sdk` | 🟢 Migrated |  Android, iOS     | KMP play integrity provider wrapper. |
| `appcheck:firebase-appcheck-recaptcha`                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP recaptcha provider wrapper. |
| `ai-logic:firebase-ai`                                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `ai-logic:firebase-ai-ondevice`                       | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `ai-logic:firebase-ai-ondevice-interop`               | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-abt`                                        | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS stub).  |
| `firebase-annotations`                                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP common annotations.  |
| `firebase-appdistribution`                            | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-appdistribution-api`                        | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-common`                                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-components`                                 | `sdk` | 🟢 Migrated |  Android, iOS     | KMP common components.   |
| `firebase-components:firebase-dynamic-module-support` | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS stub).  |
| `firebase-config`                                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-config-interop`                             | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-crashlytics`                                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-crashlytics-ndk`                            | `sdk` | 🟢 Migrated |  Android, iOS     | KMP NDK support wrapper.   |
| `firebase-database`                                   | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-database-collection`                        | `sdk` | 🟢 Migrated |  Android, iOS     | KMP sorted collections helper. |
| `firebase-dataconnect`                                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-dataconnect:connectors`                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-datatransport`                              | `sdk` | 🟢 Migrated |  Android, iOS     | KMP internal support shell. |
| `firebase-functions`                                  | `sdk` | 🟢 Migrated |  Android, iOS     | KMP SwiftPM wrapper.     |
| `firebase-messaging`                                  | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-messaging-directboot`                       | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-inappmessaging`                             | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-inappmessaging-display`                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-installations-interop`                      | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-installations`                              | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-ml-modeldownloader`                         | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-perf`                                       | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-sessions`                                   | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-storage`                                    | `sdk` | 🟢 Migrated |  Android, iOS     | KMP SwiftPM wrapper.     |
| `protolite-well-known-types`                          | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `encoders:firebase-encoders`                          | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `encoders:firebase-encoders-json`                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `encoders:firebase-encoders-processor`                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `encoders:firebase-encoders-proto`                    | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `encoders:firebase-encoders-reflective`               | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `encoders:firebase-decoders-json`                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `encoders:protoc-gen-firebase-encoders`               | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `transport:transport-api`                             | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `transport:transport-backend-cct`                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `transport:transport-runtime`                         | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `transport:transport-runtime-testing`                 | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |

---

## 🛠️ KMP Conversion Checklist for Pending Modules

To convert any pending module (`firebase-xxx`) into KMP:
1. Replace `id 'kotlin-android'` with `id 'org.jetbrains.kotlin.multiplatform'` in the gradle script.
2. Define `androidTarget()` and native targets (e.g. `iosArm64()`, `iosSimulatorArm64()`).
3. Setup SwiftPM dependencies block if linking Objective-C/Swift binary.
4. Establish `src/commonMain/kotlin` for generic APIs.
5. Point the `androidMain` source directory back to `src/main/java`.

---

## 📜 Recent Migration History

### 2026-07-02: `firebase-crashlytics-ndk` KMP Module Creation & Android NDK Crash Capture support wrapper
* **KMP Module Realization**: Created the new `:firebase-crashlytics-ndk` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed expect class `FirebaseCrashlyticsNdk` inside `commonMain` to support verification of NDK crash capture enablement status.
* **Platform Factory Bindings**: Delegates to Android's official `com.google.firebase.crashlytics.ndk` components on Android Target. Provides an unsupported exception on iOS since Apple platforms record native C/C++ crashes directly inside the core runner framework instead of requiring a separate NDK layer. SwiftPM is **not** required.
* **Interactive NDK Verification**: Expanded `CrashlyticsScreen` inside the Sample App to display dynamic capture statuses, and detail NDK configuration instructions (CMake, ndk-build, symbol upload commands).
### 2026-07-01: `appcheck:firebase-appcheck-recaptcha` KMP Module Creation & Web-centric reCAPTCHA Provider Wrapper
* **KMP Module Realization**: Created the new `:appcheck:firebase-appcheck-recaptcha` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed expect class `RecaptchaAppCheckProviderFactory` inside `commonMain` conforming to `AppCheckProviderFactory`.
* **Platform Factory Bindings**: Delegates to Android's official `com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory` on Android Target. Provides an unsupported exception on iOS since Apple platforms use native AppAttest/DeviceCheck options instead of Google reCAPTCHA. SwiftPM is **not** required.
* **Dependency Reference Check**: Added `:appcheck:firebase-appcheck-recaptcha` compile reference in `composeApp` and `AppCheckRecaptchaTest` helper to verify core factory contract compilation and safe iOS runtime exception handling.

### 2026-07-01: `appcheck:firebase-appcheck-playintegrity` KMP Module Creation & Android Play Integrity Provider Wrapper
* **KMP Module Realization**: Created the new `:appcheck:firebase-appcheck-playintegrity` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed expect class `PlayIntegrityAppCheckProviderFactory` inside `commonMain` conforming to `AppCheckProviderFactory`.
* **Platform Factory Bindings**: Delegates to Android's official `com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory` on Android Target. Provides an unsupported exception on iOS since Apple platforms use other native verification solutions (e.g., DeviceCheck/AppAttest) instead of Google Play Services. SwiftPM is **not** required.
* **Interactive Verification Screen**: Added `PlayIntegrityScreen` in the Sample App enabling user-facing verification, environment setup prerequisites guidelines (SHA-256 fingerprint, Console configuration), and dynamic provider installation checks.

### 2026-07-01: `appcheck:firebase-appcheck-interop` KMP Module Creation & Interop contract wrapper
* **KMP Module Realization**: Created the new `:appcheck:firebase-appcheck-interop` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed expect interfaces `InteropAppCheckTokenProvider` and `AppCheckTokenListener`, and model class `AppCheckTokenResult` inside `commonMain`, delegating to official AppCheck Interop dependencies on Android and SwiftPM `FirebaseAppCheckInterop` products on iOS (`FIRAppCheckInteropProtocol` etc.).
* **Asynchronous Coroutine Bridging**: Implemented Task listener awaiting on Android and completion-block to Coroutine suspension on iOS for token retrieval APIs.
* **Dependency Reference Check**: Added `:appcheck:firebase-appcheck-interop` compile reference in `composeApp` and `AppCheckInteropTest` helper to verify core interop boundary contract compilation.

### 2026-07-01: `firebase-database` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:firebase-database` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed clean `expect` classes `FirebaseDatabase`, `DatabaseReference`, `DataSnapshot` inside `commonMain`, delegating seamlessly to official BOM Database dependencies on Android and SwiftPM `FirebaseDatabase` products on iOS (`FIRDatabase` etc.).
* **Asynchronous Coroutine Bridging**: Implemented Task listener awaiting on Android and Completion-block to Coroutine suspension on iOS for setValue, removeValue, and updateChildren APIs.

### 2026-07-01: `firebase-database-collection` KMP Module Creation & Helper data structure
* **Kotlin-only Data Structure**: Created the new `:firebase-database-collection` module from scratch. Implemented pure Kotlin immutable sorted map (`ImmutableSortedMap`) and set (`ImmutableSortedSet`) inside `commonMain` using standard list-binary-search, achieving 100% platform-agnostic performance.
* **Android Backward Compatibility**: Set `api("com.google.firebase:firebase-database-collection")` dependency on Android Target to retain binary interoperability.
* **Interactive Sorted Map Screen**: Added `DatabaseCollectionScreen` in the Sample App allowing real-time insertion, dynamic sorting, and key-removal visualization.

### 2026-07-01: `firebase-datatransport` KMP Module Creation & Internal support shell
* **Minimal Infrastructure Shell**: Created the new `:firebase-datatransport` module from scratch containing a thin expectation registrar `TransportRegistrar`.
* **Android Registrar Association**: Linked actual class to Android's official `com.google.firebase.datatransport.TransportRegistrar` to support native components registration automatically.
* **iOS Compatibility Shell**: Designed a dummy ios actual shell keeping iOS target fully compiling, as event dispatching is natively packed inside feature SDK products. SwiftPM is **not** required.

### 2026-07-01: `appcheck:firebase-appcheck` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:appcheck:firebase-appcheck` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed clean `expect` classes `FirebaseAppCheck`, `AppCheckToken`, and `AppCheckProviderFactory` inside `commonMain`, delegating to official AppCheck dependencies on Android and SwiftPM `FirebaseAppCheck` products on iOS (`FIRAppCheck` etc.).
* **Asynchronous Coroutine Bridging**: Implemented Task listener awaiting on Android and completion-block to Coroutine suspension on iOS for token retrieval APIs.
* **Dependency Reference Check**: Added `:appcheck:firebase-appcheck` compile reference in `composeApp` and `AppCheckTest` helper to verify core API compilation.

### 2026-07-01: `appcheck:firebase-appcheck-debug` KMP Module Creation & Debug Provider Factory Wrapper
* **KMP Module Realization**: Created the new `:appcheck:firebase-appcheck-debug` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed expect class `DebugAppCheckProviderFactory` inside `commonMain` conforming to `AppCheckProviderFactory`.
* **Platform Factory Bindings**: Delegates to Android's official `com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory` on Android Target and SwiftPM's `FIRAppCheckDebugProviderFactory` on iOS Target.
* **Dependency Reference Check**: Added `:appcheck:firebase-appcheck-debug` compile reference in `composeApp` and `AppCheckDebugTest` helper to verify debug factory compilation and installation.

### 2026-07-01: `appcheck:firebase-appcheck-debug-testing` KMP Module Creation & Debug Testing Helper Wrapper
* **KMP Module Realization**: Created the new `:appcheck:firebase-appcheck-debug-testing` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed expect class `DebugAppCheckTestHelper` inside `commonMain` to support programmatic debug token injection.
* **Platform Helpers Bindings**: Delegates to Android's official `com.google.firebase.appcheck.debug.testing.DebugAppCheckTestHelper` on Android Target. Provides an unsupported exception on iOS since iOS configures debug tokens via static startup arguments rather than runtime Helper API. SwiftPM is **not** required.
* **Dependency Reference Check**: Added `:appcheck:firebase-appcheck-debug-testing` compile reference in `composeApp` and `AppCheckDebugTestingTest` helper to verify debug testing compilation and safe iOS runtime exception handling.
