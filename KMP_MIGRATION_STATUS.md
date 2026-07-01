# Firebase Android SDK - Kotlin Multiplatform (KMP) Migration Status

This document tracks the KMP migration status across all subprojects defined in the repository.

---

## 📊 Migration Summary

- **Total SDKs**: 35
- **KMP Enabled**: 6
- **Android Native Only**: 29

---

## 📋 Subprojects Migration Matrix

| Subproject Path                                       | Type  | KMP Support | Targets Supported | Notes                    |
|:------------------------------------------------------|:-----:|:-----------:|:-----------------:|:-------------------------|
| `firebase-firestore`                                  | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `appcheck:firebase-appcheck`                          | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `appcheck:firebase-appcheck-debug`                    | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `appcheck:firebase-appcheck-debug-testing`            | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `appcheck:firebase-appcheck-interop`                  | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `appcheck:firebase-appcheck-playintegrity`            | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `appcheck:firebase-appcheck-recaptcha`                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
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
| `firebase-crashlytics`                                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-crashlytics-ndk`                            | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
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
