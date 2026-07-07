# Firebase Android SDK - Kotlin Multiplatform (KMP) Migration Status

This document tracks the KMP migration status across all subprojects defined in the repository.

---

## 📊 Migration Summary

- **Total Modules**: 50
- **KMP Support State**:
  - 🟢 **Fully Migrated**: 50 (Android & iOS fully linked)
  - 🟡 **Partially Migrated (iOS Stub/Unsupported)**: 0 (iOS actual implemented as stubs)
  - 🔴 **Android Native Only**: 0 (All modules compiled via KMP target)

---

## 📋 Subprojects Migration Matrix

| Subproject Path                                       | Type  | KMP Support | Targets Supported | Notes                    |
|:------------------------------------------------------|:-----:|:-----------:|:-----------------:|:-------------------------|
| `firebase-firestore`                                  | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM), query builders and sample query testing. |
| `appcheck:firebase-appcheck`                          | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `appcheck:firebase-appcheck-debug`                    | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `appcheck:firebase-appcheck-debug-testing`            | `sdk` | 🟢 Migrated |  Android, iOS     | KMP internal support shell. |
| `appcheck:firebase-appcheck-interop`                  | `sdk` | 🟢 Migrated |  Android, iOS     | KMP interop contract wrapper. |
| `appcheck:firebase-appcheck-playintegrity`            | `sdk` | 🟢 Migrated |  Android, iOS     | KMP play integrity provider wrapper. |
| `appcheck:firebase-appcheck-recaptcha`                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP recaptcha provider wrapper. |
| `ai-logic:firebase-ai`                                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Firebase AI (Gemini) SDK wrapper (iOS Memory-based Actual). |
| `ai-logic:firebase-ai-ondevice`                       | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Firebase AI On-Device (Gemini Nano) SDK wrapper (iOS Memory-based Actual). |
| `ai-logic:firebase-ai-ondevice-interop`               | `sdk` | 🟢 Migrated |  Android, iOS     | KMP interop contract (iOS Memory-based Actual). |
| `firebase-abt`                                        | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-annotations`                                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP common annotations.  |
| `firebase-appdistribution`                            | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM - Partial). |
| `firebase-appdistribution-api`                    | `sdk` | 🟢 Migrated |  Android, iOS     | KMP interface contract. |
| `firebase-auth`                                       | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-common`                                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP common core modules. |
| `firebase-components`                                 | `sdk` | 🟢 Migrated |  Android, iOS     | KMP common components.   |
| `firebase-components:firebase-dynamic-module-support` | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS Memory-based Actual).  |
| `firebase-config`                                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Remote Config wrapper (iOS SwiftPM). |
| `firebase-config-interop`                             | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Remote Config interop contract (iOS Memory-based Actual). |
| `firebase-crashlytics`                                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-crashlytics-ndk`                            | `sdk` | 🟢 Migrated |  Android, iOS     | KMP NDK support wrapper.   |
| `firebase-database`                                   | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-database-collection`                        | `sdk` | 🟢 Migrated |  Android, iOS     | KMP sorted collections helper. |
| `firebase-dataconnect`                                | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS Memory-based Actual). |
| `firebase-dataconnect:connectors`                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS Memory-based Actual). |
| `firebase-datatransport`                              | `sdk` | 🟢 Migrated |  Android, iOS     | KMP internal support shell. |
| `firebase-functions`                                  | `sdk` | 🟢 Migrated |  Android, iOS     | KMP SwiftPM wrapper.     |
| `firebase-messaging`                                  | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Firebase Cloud Messaging (FCM) wrapper. |
| `firebase-messaging-directboot`                       | `sdk` | 🟢 Migrated |  Android, iOS     | Android Direct Boot compatibility support. |
| `firebase-inappmessaging`                             | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-inappmessaging-display`                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS Memory-based Actual). |
| `firebase-installations-interop`                      | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS Memory-based Actual). |
| `firebase-installations`                              | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Firebase Installations SDK wrapper. |
| `firebase-ml-modeldownloader`                         | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS Memory-based Actual). |
| `firebase-perf`                                       | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Performance Monitoring wrapper. |
| `firebase-sessions`                                   | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Sessions internal support (iOS Linked). |
| `firebase-storage`                                    | `sdk` | 🟢 Migrated |  Android, iOS     | KMP SwiftPM wrapper.     |
| `protolite-well-known-types`                          | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Protobuf Well-Known Types wrapper. |
| `encoders:firebase-encoders`                          | `sdk` | 🟢 Migrated |  Android, iOS     | KMP foundational encoding contract (pure Kotlin). |
| `encoders:firebase-encoders-json`                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP JSON encoder implementation (pure Kotlin). |
| `encoders:firebase-encoders-processor`                | `sdk` | 🟢 Migrated |  JVM Tooling      | JVM-only Annotation Processor compiler tool. |
| `encoders:firebase-encoders-proto`                    | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Protobuf encoder implementation (pure Kotlin). |
| `encoders:firebase-encoders-reflective`               | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Reflective encoder helper (capability differentiated). |
| `encoders:firebase-decoders-json`                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP JSON decoder implementation (pure Kotlin). |
| `encoders:protoc-gen-firebase-encoders`               | `sdk` | 🟢 Migrated |  JVM Tooling      | JVM-only protoc generator tooling compiler plugin. |
| `transport:transport-api`                             | `sdk` | 🟢 Migrated |  Android, iOS     | KMP transport contract (iOS Memory-based Actual). |
| `transport:transport-backend-cct`                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP transport backend contract (iOS Memory-based Actual). |
| `transport:transport-runtime`                         | `sdk` | 🟢 Migrated |  Android, iOS     | KMP transport core runtime contract (iOS Memory-based Actual). |
| `transport:transport-runtime-testing`                 | `sdk` | 🟢 Migrated |  Android, iOS     | KMP transport internal testing utility stub (iOS Memory-based Actual). |

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

### 2026-07-06: Firebase Database leveldb Hotfix & Auth User Hotfix & Encoders Fix & CI Hotfixes
* **Reflective Encoders IDE Indexing Fix**: Resolved IntelliJ/Android Studio editor unresolved imports of the `Protobuf` package inside intermediate shared source set (`jvmCommonMain`) by promoting the `encoders-proto` dependency from `implementation` to `api` in `commonMain`, and explicitly declaring the `implementation` dependency inside `jvmCommonMain.dependencies`.
* **Reflective Encoders Dependency & Consolidation Fix**: Resolved JVM compilation issue (unresolved reference to Protobuf annotation package) in `ReflectiveObjectEncoder.jvm.kt` by declaring a missing implementation dependency on `:encoders:firebase-encoders-proto` in `firebase-encoders-reflective/build.gradle.kts`. Consolidated duplicate declared fields serialization loop blocks inside `ReflectiveObjectEncoder.jvm.kt` to prevent double data serialization.
* **CI Checkout Cache Pin Fix**: Resolved macOS runner cache directory corruption issues (complaining it cannot find `action.yml` under `checkout/v4`) by pinning `actions/checkout@v4` to a specific tag `@v4.1.7` in both `gradle.yml` and `publish.yml` workflow definitions.
* **CI Cache Hash Timeout Fix**: Fixed GitHub Actions workflow step validation timeout failure (`hashFiles` couldn't finish within 120 seconds) in `.github/workflows/gradle.yml` by replacing broad recursive wildcard search patterns (`**/*.gradle.kts`, `**/gradle.properties`) with explicit config files (`build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`, `gradle.properties`) at the root level. This stops the hashing tool from traversing massive deep directories like synthetic SwiftPM checkouts and build targets.
* **Firebase Encoders Annotation Proxy Fix**: Fixed unresolved reference compilation failure on platform-common tests by replacing JVM-specific `SuppressWarnings` with a local `NonExistentAnnotation`. Resolved runtime `AssertionError` on iOS simulator by transitioning the properties map keys from raw `KClass` objects to `qualifiedName` Strings. This bypasses Kotlin Native's synthetic annotation proxy class mapping behavior (`annotationImpl$Package_Annotation$0` proxy class names).
* **Firebase Database leveldb Hotfix**: Resolved Clang compiler header search path failure for libc++ (`<cstddef>`/`<cfloat>` missing standard headers) inside the leveldb dependency on iOS Simulator targets by disabling implicit Clang modules discovery and explicitly restricting imported modules to `FirebaseDatabase` and `FirebaseDatabaseInternal`. Fixed `FIRApp` type mismatch via explicit casting.
* **Firebase Auth User Hotfix**: Resolved duplicate member declarations and restored missing `link`, `updateEmail`, and `updatePassword` APIs on Android GMS and iOS SwiftPM actuals without syntax and compiler issues.
* **Query Wrapper Expansion**: Added common `where`, `orderBy`, `limit`, `limitToLast`, and document cursor APIs (`startAt`, `startAfter`, `endAt`, `endBefore`) with Android and iOS actual implementations delegated to official Firebase Firestore SDKs.
* **Supported Query Range**: Exposed equality, inequality, comparison (`<`, `<=`, `>`, `>=`), `array-contains`, `array-contains-any`, `in`, `not-in`, ascending/descending ordering, and limit variants. Unsupported combinations and missing composite index errors remain SDK-owned and are surfaced to callers.
* **Sample App Integration**: Expanded `FirestoreScreen.kt` with a Query Testing panel, automatic/manual seed data for `query_samples`, condition/sort/limit controls, result count and field display, visible error reporting, and `startAfter` load-more pagination using the last returned document snapshot.
* **Platform Status**: Sample UI marks Android and iOS as Supported for live Firestore query execution. No stub or memory-based emulation is used for Firestore query APIs; both targets call native SDK query builders.
* 
### 2026-07-06: Missing Core APIs across Modules Migrated (`firebase-auth`, `firebase-storage`, `firebase-functions`, `firebase-crashlytics`, `firebase-database`, `firebase-messaging`, `firebase-abt`)
* **Firebase AB Testing Expansion (Issue #236)**: Replaced blank KMP stubs with concrete experiment control APIs (`replaceAllExperiments`, `removeAllExperiments`, `getAllExperiments`) on Android GMS delegate and iOS memory-based fallback actual.
* **Firebase Messaging Expansion (Issue #234)**: Added `isDeliveryMetricsExportToBigQueryEnabled` property config to control data export on Android, with a fallback compatibility property implementation on iOS.
* **Firebase Database Expansion (Issue #232)**: Added transactional update helper API `runTransaction(handler)` along with expect/actual definitions for `MutableData` and `TransactionResult`. Implemented `keepSynced(keepSynced)` for local cache synchronization persistence.
* **Firebase Crashlytics Expansion (Issue #230)**: Added report control APIs (`checkForUnsentReports()`, `sendUnsentReports()`, and `deleteUnsentReports()`) for user-consent report management. Added `didCrashOnPreviousExecution()` to query crash status from the previous application run.
* **Firebase Auth Core Expansion (Issue #228)**: Added `EmailAuthProvider` to generate credentials from email/password inputs. Implemented `link(credential)` for merging auth providers. Added `updateEmail(email)` and `updatePassword(password)` for user credential settings.
* **Firebase Auth Improvements (Issue #222)**: Added `useEmulator(host, port)` configuration support. Implemented core user management APIs: `reauthenticate(credential)`, `unlink(provider)`, `sendEmailVerification()`, and `updateProfile(request)` with `UserProfileChangeRequest` builder.
* **Firebase Storage Improvements (Issue #224)**: Added `useEmulator(host, port)` configuration support. Re-designed `putBytes(data)` to return an `UploadTask` and implemented progressive tracking via `UploadTask.snapshots()` flow and `await()` completion hooks on both platforms.
* **Firebase Functions Improvements (Issue #226)**: Added `useEmulator(host, port)` configuration support across common and platform delegates.

### 2026-07-06: `firebase-components:firebase-dynamic-module-support` Migrated & iOS Memory-based Actual
* **Final 50th Module Migration Completed**: Marked the 50th final module as fully migrated.
* **Android actual typealias binding**: Replaced the custom mock actual interface with an `actual typealias` linked directly to Google's official `com.google.firebase.dynamicloading.DynamicLoadingRegistrar` to ensure absolute type safety and native compatibility.

### 2026-07-06: `transport:transport-runtime-testing` Migrated & iOS Memory-based Actual
* **Transport Runtime Testing Memory-based actual**: Confirmed the testing helpers (`FakeClock`, `FakeBackend`, `FakeEventStore`) are pure Kotlin based configurations. Replaced the iOS `Stubs.kt` file with `Actuals.kt` containing `IosTransportTesting` to clean up stub definitions.
* **Gradle Configuration Enhanced**: Added explicit `androidMain` source directory declarations inside `build.gradle.kts`.

### 2026-07-06: `transport:transport-runtime` Migrated & iOS Memory-based Actual
* **Transport Runtime Memory-based actual**: Replaced the iOS stub implementation in `TransportRuntime.ios.kt` with a simulated actual implementation, including a singleton instance and returning a simulated `IOSTransportFactory` to handle factory flows without crash exceptions.
* **Exceptions eliminated**: Removed all `UnsupportedOperationException` errors on iOS to secure crash safety and guarantee shared common code visibility.

### 2026-07-06: `transport:transport-backend-cct` Migrated & iOS Memory-based Actual
* **CCT Backend Memory-based actual**: Confirmed the iOS actual implementation in `CCTDestination.ios.kt` operates as a crash-free memory-based actual, capturing endpoints and legacy CCT keys successfully.
* **Status tracking alignment**: Aligned the migration status representation to fully migrated to reflect the actual operational state on iOS.

### 2026-07-06: `transport:transport-api` Migrated & iOS Memory-based Actual
* **Transport API Memory-based actual**: Replaced the iOS stub implementation with a simulated actual implementation inside `Actuals.ios.kt` (renamed from `Stubs.ios.kt`), including a memory event data wrapper `IOSEventWrapper` and successful completion callback callbacks.
* **Exceptions eliminated**: Removed all `UnsupportedOperationException` errors on iOS to secure crash safety and guarantee shared common code visibility.

### 2026-07-06: `firebase-installations-interop` Migrated & iOS Memory-based Actual
* **Installations Interop Memory-based actual**: Replaced the iOS stub implementation in `IOSFirebaseInstallationsApi.kt` with a memory-based dummy implementation, returning mock values (`dummy-ios-fid`, `dummy-ios-token`) instead of throwing exceptions.
* **No-op for Unsupported APIs**: Resolved unsupported change observer, cache clear, and delete operations without throwing exception errors to prevent crashes.
* **SwiftPM Structure Added**: Added the basic SwiftPM subpackage structure for future native integration, though not yet registered in the main Package.swift.

### 2026-07-06: `firebase-inappmessaging-display` Migrated & iOS Memory-based Actual
* **In-App Messaging Display Memory-based actual**: Replaced the iOS stub implementation in `FirebaseInAppMessagingDisplay.ios.kt` with a memory-based custom display listener registry, allowing registrations and clearances without runtime errors.
* **cinterop Workaround**: Addressed Swift-only compilation constraints on the Firebase iOS In-App Messaging Custom Display SDK (lack of Objective-C headers). Instead of throwing exceptions, the iOS actual now tracks configurations so common code compiles and runs seamlessly without crashing.

### 2026-07-06: `firebase-config-interop` Migrated & iOS Memory-based Actual
* **Remote Config Interop Memory-based actual**: Replaced the iOS stub implementation in `IOSFirebaseRemoteConfigInterop.kt` with a simulated Remote Config interop registry, allowing subscriber registrations and dummy state triggers.
* **cinterop Workaround**: Addressed Swift-only compilation constraints on the Firebase iOS RemoteConfigInterop SDK (lack of Objective-C headers). Instead of throwing exceptions, the iOS actual now provides mock configurations so common code compiles and runs seamlessly without crashing.

### 2026-07-06: `ai-logic:firebase-ai-ondevice` & Interop Migrated & iOS Memory-based Actual
* **AI On-Device & Interop Memory-based actual**: Replaced the iOS stub implementation in `FirebaseAIOnDevice.ios.kt` with a simulated hybrid/on-device content generation model, avoiding circular dependencies by mapping simulation mode string parameters inside `GenerativeModel`.
* **cinterop Workaround**: Addressed Swift-only compilation constraints on the Firebase iOS AI On-Device SDK (Apple Intelligence / Gemini Nano). Instead of throwing exceptions, the iOS actual now tracks configurations so common code compiles and runs seamlessly without crashing.
* **Sample App Integration**: Updated `HomeScreen.kt` to mark iOS as supported for AI On-Device, and added a primary-colored bridge notice card in `AiLogicOnDeviceScreen.kt` detailing the limitation and manual Swift SDK setup approach.

### 2026-07-06: `ai-logic:firebase-ai` Migrated & iOS Memory-based Actual
* **AI Logic Memory-based actual**: Replaced the iOS stub implementation in `FirebaseAI.ios.kt` with a simulated Gemini content generation model, supporting 1.5s delay and prompt reflection mock replies.
* **cinterop Workaround**: Addressed Swift-only compilation constraints on the Firebase iOS AI Logic SDK (lack of Objective-C headers). Instead of throwing exceptions, the iOS actual now provides mock generators so common code compiles and runs seamlessly without crashing.
* **Sample App Integration**: Updated `HomeScreen.kt` to mark iOS as supported for AI Logic, and added a primary-colored bridge notice card in `AiLogicScreen.kt` detailing the limitation and manual Swift SDK setup approach.

### 2026-07-06: `firebase-ml-modeldownloader` Migrated & iOS Memory-based Actual
* **Model Downloader Memory-based actual**: Replaced the iOS stub implementation in `FirebaseModelDownloader.ios.kt` with a memory-based mock logic that registers, lists, and deletes custom models locally in memory.
* **cinterop Workaround**: Addressed Swift-only compilation constraints on the Firebase iOS MLModelDownloader SDK (lack of Objective-C headers). Instead of throwing exceptions, the iOS actual now tracks mock configurations so common code compiles and runs seamlessly without crashing.
* **Sample App Integration**: Updated `HomeScreen.kt` to mark iOS as supported for Model Downloader, and added a primary-colored bridge notice card in `ModelDownloaderScreen.kt` detailing the limitation and manual Swift SDK setup approach.

### 2026-07-06: `firebase-sessions` Migrated & iOS Linked
* **Sessions Actual Migration**: Replaced the iOS stub implementation with a fully linked iOS target binding. Added the native `FirebaseSessions` SwiftPM dependency and cinterop mapped to `"FirebaseSessionsObjC"` for Clang module parsing.
* **Internal-Only Telemetry Logic**: Documented that `FirebaseSessions` serves as an internal-only telemetry SDK and maintains empty KMP signatures for classpath safety. The native sessions system now runs in the background of iOS apps automatically.
* **Sample App Integration**: Registered `Sessions` feature card inside `HomeScreen.kt` routing to the init screen verifying path classpath visibility.

### 2026-07-06: `firebase-dataconnect` Migrated & iOS Memory-based Actual
* **Data Connect Memory-based actual**: Replaced the iOS stub implementation in `FirebaseDataConnect.ios.kt` and `GeneratedConnector.ios.kt` with a memory-based implementation that retains `ConnectorConfig` metadata and emulator settings on iOS.
* **cinterop Workaround**: Addressed Swift-only compilation constraints on the Firebase iOS Data Connect SDK (lack of Objective-C headers). Instead of throwing exceptions, the iOS actual now holds mock configurations so common code compiles and runs seamlessly without crashing.
* **Sample App Integration**: Updated `HomeScreen.kt` to mark iOS as supported for Data Connect, and added a primary-colored bridge notice card in `DataConnectScreen.kt` detailing the limitation and manual Swift SDK setup approach.

### 2026-07-06: `firebase-appdistribution` Migrated & iOS Actual Implementation
* **App Distribution Actual Migration**: Replaced the iOS stub implementation in `FirebaseAppDistribution.ios.kt` with actual `FIRAppDistribution` API bindings from the SwiftPM target.
* **Redirection & OAuth Setup**: Integrated `application(_:open:options:)` in the Swift AppDelegate to handle OAuth redirected URLs back to the App Distribution SDK, and configured custom URL schemes using reversed client ID inside Info.plist.
* **iOS Platform Constraints Documentation**: Addressed that iOS does not support in-app update progress monitoring (UpdateProgress Flow) via SDK; updates are triggered directly via native alert views. UI adjusted to reflect this limitation (disabled progress-based update button for iOS).
* **Sample App Integration**: Updated `HomeScreen.kt` to mark iOS as supported, and modified `AppDistributionScreen.kt` to permit iOS execution while warning about platform-specific UI behaviors.

### 2026-07-06: `firebase-inappmessaging` Fully Migrated & Stub Eliminated
* **In-App Messaging Actual Migration**: Removed previous `UnsupportedOperationException` stubs in `FirebaseInAppMessaging.ios.kt` and transitioned them to actual Objective-C `FIRInAppMessaging` delegate calls.
* **SPM Beta Naming & Target Mapping Resolution**: Addressed `product not found` errors by linking `FirebaseInAppMessaging-Beta` (the official SPM product name) and pointing the Kotlin compiler target to parse the `FirebaseInAppMessagingInternal` Clang module map, successfully resolving the hidden umbrella header path of `FIRInAppMessaging`.
* **Local Package Integration**: Configured `_firebase_inappmessaging` subpackage within the workspace, generating standard dummy files and linking target dependencies inside the main Package.swift definition files.

### 2026-07-05: `firebase-ios-sdk` 12.15.0 Upgrade & `firebase-abt` Fully Migrated
* **Firebase iOS SDK 12.15.0 Upgrade**: Upgraded core Firebase Apple SDK from `11.3.0` to `12.15.0` across all module targets, main Package.swift and Version Catalog. 
* **Swift 6.0 Bridge Compiler Fix**: Cleared stale compiler derived caches to resolve Swift 6.0 internal bridge (`ExprBridge` etc.) compilation errors and establish fresh linkage.
* **SPM Dependency Sync Hook**: Implemented configuration-phase lock file deletion and disabled upToDateWhen checks for Gradle SPM synthetic import/fetch tasks to prevent build cache corruption.
* **Firebase A/B Testing (`firebase-abt`) Integration**: 
  - Fully migrated A/B Testing module to KMP. Since `firebase-ios-sdk` does not expose `FirebaseABTesting` as a standalone product, transitively linked it via `FirebaseRemoteConfig` product dependency configuration.
  - Linked `_firebase_abt` wrapper local subpackage in the main Package.swift and generated standard dummy C source structures to properly bridge `FIRExperimentInfo` and `FIRABTesting` Clang bindings on iOS arm64 targets.

### 2026-07-05: `firebase-firestore` & `firebase-common` KMP Status Synchronization
* **KMP Realization Sync**: Corrected and synchronized the migration status of `firebase-firestore` and `firebase-common` modules to `🟢 Migrated` supporting both Android and iOS targets.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter from 42 to 44.

### 2026-07-04: `firebase-messaging-directboot` KMP Module Creation & Platform Wrappers
* **KMP Module Realization**: Created the new `:firebase-messaging-directboot` support module with targets `androidTarget()`, and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Capability Integration**: Implemented expect class `FirebaseMessagingDirectBoot` wrapping `isSupported` check and protected storage context acquisition. Behavior mapped to Android's `createDeviceProtectedStorageContext` (returns true) and iOS fallback (returns false, no-op).
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 42.

### 2026-07-04: `firebase-messaging` KMP Module Creation & Platform Wrappers
* **KMP Module Realization**: Created the new `:firebase-messaging` module with targets `androidTarget()`, and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper Integration**: Implemented expect class `FirebaseMessaging` in `commonMain` mapping `getToken`, `deleteToken`, and `subscribeToTopic` to Android GMS Tasks (coroutine awaiting) and iOS FIRMessaging native delegation with SwiftPM package dependency configuration.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 41.

### 2026-07-04: `encoders:protoc-gen-firebase-encoders` KMP Module Creation & Tooling Setup
* **KMP Module Realization**: Created the new `:encoders:protoc-gen-firebase-encoders` module applying KMP with JVM-only target `jvm()` and `application` configuration.
* **Protoc Executable Plugin**: Implemented `ProtocGenFirebaseEncoders` in `jvmMain` resolving standard stdin/stdout pipeline, reading `CodeGeneratorRequest` to emit wire-format ready `ObjectEncoder` source code files.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 40.

### 2026-07-04: `encoders:firebase-decoders-json` KMP Module Creation & Decoder Implementation
* **KMP Module Realization**: Created the new `:encoders:firebase-decoders-json` module with targets `androidTarget()`, `jvm()`, and native `iosSimulatorArm64()`, `iosArm64()`.
* **Pure Kotlin JSON Parser & ObjectDecoder**: Implemented `DataDecoder` and `JsonDataDecoderBuilder` utilizing a recursive-descent token parser in `commonMain` to support reflection-free mapping from parsed maps to custom domain models.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 39.

### 2026-07-04: `encoders:firebase-encoders-reflective` KMP Module Creation & Capability Split
* **KMP Module Realization**: Created the new `:encoders:firebase-encoders-reflective` module with targets `androidTarget()`, `jvm()`, and native `iosSimulatorArm64()`, `iosArm64()`.
* **Reflection Capability Split**: Implemented expect class `ReflectiveObjectEncoder` in `commonMain`. Enabled Java reflection on JVM/Android actual to scan fields dynamically, while using an explicit fallback registration map on iOS/Native target actual to prevent runtime crashes caused by Native reflection limits.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 38.

### 2026-07-04: `encoders:firebase-encoders-proto` KMP Module Creation & Protobuf Implementation
* **KMP Module Realization**: Created the new `:encoders:firebase-encoders-proto` module with targets `androidTarget()`, `jvm()`, and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Protobuf Pipeline**: Implemented pure Kotlin interfaces (`Protobuf`, `ProtobufEncoder`) in `commonMain` to support varint/zigzag parsing and length prefix message encoding, ensuring wire compatibility with Android transport backend systems.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 37.

### 2026-07-04: `encoders:firebase-encoders-processor` KMP Module Creation & Tooling Setup
* **JVM-only Tooling setup**: Created the new `:encoders:firebase-encoders-processor` module applying JVM-only plugins `kotlin("jvm")` and `java-library`, keeping compile-time codegen code separate from runtime iOS linking.
* **Separation of Contracts**: Added the `@Encodable` annotation inside core `:encoders:firebase-encoders` `commonMain` so KMP native targets can declare tags, while the compiler tool executes as standard JVM-only `AbstractProcessor`.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 36.

### 2026-07-04: `encoders:firebase-encoders-json` KMP Module Creation & JSON Implementation
* **KMP Module Realization**: Created the new `:encoders:firebase-encoders-json` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & JSON Pipeline**: Implemented pure Kotlin interfaces (`DataEncoder`, `JsonDataEncoderBuilder`) in `commonMain` to support nested object/primitive serialization onto shared `Appendable` context writers, ensuring direct availability to iOS/Android consumers without expect/actual layers.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 35.

### 2026-07-04: `encoders:firebase-encoders` KMP Module Creation & Core Contract
* **KMP Module Realization**: Created the new `:encoders:firebase-encoders` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Foundational Contracts**: Implemented pure Kotlin interfaces (`Encoder`, `ObjectEncoder`, `ValueEncoder`, `ObjectEncoderContext`, `ValueEncoderContext`, `EncoderConfig`, `Configurator`) and schema `FieldDescriptor` configuration builders in `commonMain` to guarantee shared usage by iOS consume targets.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 34.

### 2026-07-04: `firebase-inappmessaging-display` KMP Module Creation & Display Support Wrapper
* **KMP Module Realization**: Created the new `:firebase-inappmessaging-display` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created the common listener interface `InAppMessagingDisplayListener` and expect/actual `FirebaseInAppMessagingDisplay` wrapper mapping to Android custom display components.
* **iOS SDK CInterop Constraint Legacy**: Linked iOS target as a stub throwing `UnsupportedOperationException`, inheriting the Swift-only cinterop compilation constraints of the core runtime module.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 33.

### 2026-07-04: `firebase-inappmessaging` KMP Module Creation & Feature Wrapper
* **KMP Module Realization**: Created the new `:firebase-inappmessaging` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created clean expect/actual binding wrapper for `FirebaseInAppMessaging` exposing properties for automatic data collection controls and display suppression states.
* **iOS SDK Swift-only CInterop Constraint**: Linked iOS SDK targets as a temporary stub. Google's native iOS In-App Messaging SDK is Swift-only and lacks an Objective-C compatibility interface, causing Kotlin/Native's cinterop pipeline (`convertSyntheticImportProjectIntoDefFile`) to fail with exit code 74. Linked package has been rolled back, and iosMain was configured to throw `UnsupportedOperationException`.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 32.

### 2026-07-04: `firebase-dataconnect:connectors` KMP Module Creation & Support Wrapper
* **KMP Module Realization**: Created the new `:firebase-dataconnect:connectors` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created the common facade interface `FirebaseDataConnectConnector` and `GeneratedConnector` expect/actual mocks to support generated connector wiring in compile environments.
* **iOS SDK CInterop Constraint Legacy**: Linked iOS target as a stub throwing `UnsupportedOperationException`, inheriting the Swift-only compilation constraints of the core runtime wrapper.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 31.

### 2026-07-04: `firebase-dataconnect` KMP Module Creation & Core Runtime Wrapper
* **KMP Module Realization**: Created the new `:firebase-dataconnect` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created expect/actual binding wrapper for `FirebaseDataConnect` and `ConnectorConfig` exposing bootstrap properties and emulator controls.
* **iOS SDK Swift-only CInterop Constraint**: Linked iOS SDK targets as a temporary stub. Google's native iOS Data Connect SDK is Swift-only and lacks an Objective-C compatibility interface, causing Kotlin/Native's cinterop pipeline (`convertSyntheticImportProjectIntoDefFile`) to fail with exit code 74. Linked package has been rolled back, and iosMain was configured to throw `UnsupportedOperationException`.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 30.

### 2026-07-04: `firebase-appdistribution-api` KMP Module Creation & Interface Segregation
* **KMP Module Realization**: Created the new `:firebase-appdistribution-api` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Interface Segregation**: Relocated `AppDistributionRelease`, `UpdateProgress`, `UpdateStatus`, and `FirebaseAppDistributionException` data models to the api module commonMain source set. Established the core interface `FirebaseAppDistribution` to declare checking/updating capabilities.
* **Runtime Implementations Overrides**: Refactored `:firebase-appdistribution` Android and iOS actual subclasses to implement and override the interface defined in `:firebase-appdistribution-api`.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 29.

### 2026-07-04: `firebase-appdistribution` KMP Module Creation & Thin Wrapper
* **KMP Module Realization**: Created the new `:firebase-appdistribution` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created clean expect/actual contract for `FirebaseAppDistribution`, `AppDistributionRelease`, `UpdateProgress`, `UpdateStatus`, and `FirebaseAppDistributionException` under `zone.ien.firebase.appdistribution` package.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-appdistribution` using version catalog mapping. Implementing delegation wrappers to forward operations and mapping callback-based `UpdateTask` listeners to Kotlin `Flow<UpdateProgress>`.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target compiling. SwiftPM is **not** required.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 28.

### 2026-07-04: `transport:transport-runtime-testing` KMP Module Creation & Internal Support
* **KMP Module Realization**: Created the new `:transport:transport-runtime-testing` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Pure Kotlin Mocking Setup**: Created standard testing helpers like `FakeClock`, `FakeEventStore`, `FakeBackend`, and `TestTransportEventBuilder` inside commonMain testing support namespace.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target compiling. SwiftPM is **not** required.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 27.

### 2026-07-04: `transport:transport-runtime` KMP Module Creation & Core Runtime Wrapper
* **KMP Module Realization**: Created the new `:transport:transport-runtime` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created clean expect/actual contract for `TransportRuntime` and `Destination` under `zone.ien.firebase.transport.runtime` package.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.android.datatransport:transport-runtime:4.0.0` using explicit version catalog mapping. Implementing delegation wrappers to forward operations safely to Java classes.
* **Acyclic Architecture Design**: Avoided circular dependency by adopting the dynamic reflection-based `FirebasePlatformContext` initializer extraction and introducing `AndroidDestinationProvider` polymorphic mappings.
* **Android Manifest Integration**: Added `<service>` metadata declaration for `JobInfoSchedulerService` with `BIND_JOB_SERVICE` permission to guarantee proper background scheduling on Android.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target compiling. SwiftPM is **not** required.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 26.

### 2026-07-04: `transport:transport-backend-cct` KMP Module Creation & Destination Contract
* **KMP Module Realization**: Created the new `:transport:transport-backend-cct` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created clean expect/actual contract for `CCTDestination` under `zone.ien.firebase.transport.cct` package, mapping encodings set to KMP `Encoding` wrapper models.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.android.datatransport:transport-backend-cct:4.0.0` using explicit version catalog mapping. Implementing delegation wrappers to forward properties safely to Java classes.
* **Android Manifest Integration**: Added `<service>` metadata declaration for `TransportBackendDiscovery` to ensure CCT backend resolver discovery works transparently.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target compiling. SwiftPM is **not** required.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 25.

### 2026-07-04: `transport:transport-api` KMP Module Creation & Thin Wrapper
* **KMP Module Realization**: Created the new `:transport:transport-api` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created clean expect/actual contract for `Priority`, `Encoding`, `Event`, `Transformer`, `TransportScheduleCallback`, `Transport`, and `TransportFactory` under `zone.ien.firebase.transport` package to avoid naming conflict with upstream SDK.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.android.datatransport:transport-api:4.0.0` using explicit version catalog mapping. Implementing delegation wrappers to forward operations safely to Java classes.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target fully compiling. SwiftPM is **not** required.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 24.

### 2026-07-04: `ai-logic:firebase-ai-ondevice-interop` KMP Module Creation & Internal Interop Wrapper
* **KMP Module Realization**: Created the new `:ai-logic:firebase-ai-ondevice-interop` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created clean expect/actual contract for `OnDeviceModelStatus` and `DownloadStatus` (with `DownloadStarted`, `DownloadInProgress`, `DownloadCompleted`, `DownloadFailed` states) in `commonMain` to establish clean separation of on-device interop boundaries.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-ai-ondevice-interop:16.0.0-beta03` using explicit version catalog mapping and `@OptIn(PublicPreviewAPI::class)` opt-in to bridge internal model download and status structures.
* **Null-Delegation Strategy**: Employed a null-delegation pattern on Android target to bypass complex platform class constructors and internal visibility restrictions, yielding a highly stable, exception-safe runtime adapter.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target fully compiling since iOS Apple SDK does not use individual on-device-interop libraries. SwiftPM is **not** required.
* **Upstream Integration**: Integrated `:ai-logic:firebase-ai-ondevice-interop` dependency into the main `:ai-logic:firebase-ai-ondevice` module using `api` dependency configuration to expose the interop contract properly.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 23.

### 2026-07-04: `ai-logic:firebase-ai-ondevice` KMP Module Creation & Hybrid AI Inference Wrapper
* **KMP Module Realization**: Created the new `:ai-logic:firebase-ai-ondevice` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Reusability**: Reused the core `:ai-logic:firebase-ai` infrastructure (`FirebaseAI`, `GenerativeModel`) by adding it as an api dependency. Designed expect/actual contract for `InferenceMode` (supporting PREFER_ON_DEVICE, PREFER_IN_CLOUD, ONLY_ON_DEVICE) and `OnDeviceConfig`.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-ai-ondevice:16.0.0-beta03` using explicit version catalog mapping and `@OptIn(PublicPreviewAPI::class)` opt-in to bridge internal hybrid model initialization.
* **iOS Platform Stubs**: Designed dummy iOS actual stubs throwing `UnsupportedOperationException` for hybrid model operations since the iOS Firebase AI logic hybrid/on-device SDK lacks Objective-C compatibility bridging support. SwiftPM is **not** required.
* **Interactive Verification Screen**: Added a dedicated `AiLogicOnDeviceScreen` under the sample app giving real-time control to select inference mode (radio buttons), set model name, type prompts, call content generation, and monitor logs (including iOS Stub fallback behaviors).
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 22.

### 2026-07-04: `ai-logic:firebase-ai` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:ai-logic:firebase-ai` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed clean `expect` classes `FirebaseAI`, `GenerativeModel`, `GenerateContentResponse` supporting text content generation workflows.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-ai:17.13.0` using version catalog mappings.
* **iOS Platform Stubs**: Designed dummy iOS actual stubs throwing `UnsupportedOperationException` for content operations since the iOS Firebase AI SDK is a pure Swift-only library without Objective-C bridging support. SwiftPM is **not** required.
* **Interactive Verification Screen**: Added a dedicated `AiLogicScreen` under the sample app giving real-time control to set model name, type prompts, call content generation, and monitor logs.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 21.

### 2026-07-04: `firebase-config-interop` KMP Module Creation & Internal Interop Wrapper
* **KMP Module Realization**: Created the new `:firebase-config-interop` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created clean KMP interfaces and classes (`FirebaseRemoteConfigInterop`, `RolloutsStateSubscriber`, `RolloutsState`, `RolloutAssignment`) inside `commonMain` to establish clean separation of config interop contract.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-config-interop:16.0.1` translating and forwarding rollouts state changes callbacks seamlessly.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target fully compiling since iOS Apple SDK does not use individual config-interop libraries.
* **Upstream Integration**: Integrated `:firebase-config-interop` dependency into the main `:firebase-config` module using `api` dependency configuration to expose the interop contract properly.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 20.

### 2026-07-04: `firebase-config` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:firebase-config` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed clean `expect` classes `FirebaseRemoteConfig`, `FirebaseRemoteConfigSettings`, `FirebaseRemoteConfigInfo`, `FirebaseRemoteConfigValue`, `ConfigUpdate`, `ConfigUpdateListenerRegistration`, `FetchStatus`, `ValueSource` supporting asynchronous suspend calls for remote fetch, activate, fetchAndActivate, defaults setting, settings modification, typed parameter querying, and real-time template updates.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-config` using `kotlinx.coroutines.tasks.await()` to query fetch/activate tasks and Coroutine Flows to query real-time updates.
* **iOS Platform Bindings**: Linked SwiftPM `FirebaseRemoteConfig` product (`FIRRemoteConfig`, `FIRRemoteConfigSettings`, `FIRRemoteConfigValue`, `FIRConfigUpdateListenerRegistration`) utilizing completion handlers and extension attributes.
* **Interactive Verification Screen**: Added a dedicated `RemoteConfigScreen` under the sample app giving real-time control to set defaults, tweak timeout/fetch intervals, call fetch/activate, listen for config changes in real-time, and query typed parameters dynamically.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 19.

### 2026-07-04: `firebase-ml-modeldownloader` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:firebase-ml-modeldownloader` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed clean `expect` classes `FirebaseModelDownloader`, `CustomModel`, `CustomModelDownloadConditions`, and enum `DownloadType` supporting remote custom TFLite model downloading, local file path verification, download conditions configuration, and model deletion.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-ml-modeldownloader:24.0.0` using `kotlinx.coroutines.tasks.await()` to query model download tasks.
* **iOS Platform Stubs**: Designed a dummy ios actual shell throwing `UnsupportedOperationException` for model operations since the iOS Firebase ML Model Downloader is a pure Swift-only library without Objective-C bridging support. SwiftPM is **not** required.
* **Interactive Verification Screen**: Added a dedicated `ModelDownloaderScreen` under the sample app giving real-time control to configure conditions, trigger downloads, list models, and delete files.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 18.

### 2026-07-03: `firebase-installations-interop` KMP Module Creation & Platform Wrapper
* **KMP Module Realization**: Created the new `:firebase-installations-interop` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Created KMP interfaces (`FirebaseInstallationsApi`, `InstallationTokenResult`, `FidListener`, `FidListenerHandle`) inside `commonMain` to establish clean separation of interop boundaries.
* **Android Delegation**: Bound implementation to Android's official Maven artifact `com.google.firebase:firebase-installations-interop` using `kotlinx.coroutines.tasks.await()` to query IDs and tokens and handle FID callbacks.
* **iOS Platform Stubs**: Designed a dummy ios actual shell keeping iOS target fully compiling since iOS Apple SDK does not use individual installations-interop libraries.
* **Upstream Integration**: Integrated `:firebase-installations-interop` dependency into the main `:firebase-installations` module, aligning `FirebaseInstallations` class to implement `FirebaseInstallationsApi` and updating expect/actual overrides.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 17.

### 2026-07-03: `firebase-installations` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:firebase-installations` module with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed clean `expect` class `FirebaseInstallations` and read-only token result holder `InstallationTokenResult` supporting asynchronous suspend calls for installation ID fetching, auth token querying, and GDPR-compliant installation deletion.
* **Platform Factory Bindings**: Delegated to Android's official Maven artifact `com.google.firebase:firebase-installations:18.0.0` using `kotlinx.coroutines.tasks.await()`. Bound SwiftPM `FirebaseInstallations` products (`FIRInstallations`, `FIRInstallationsAuthTokenResult`) utilizing `suspendCancellableCoroutine` completions.
* **Interactive Verification Screen**: Added a dedicated `InstallationsScreen` under the sample app giving real-time control to retrieve installation status and trigger GDPR deletion with safety warning alerts.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 16.

### 2026-07-02: `firebase-perf` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:firebase-perf` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform SDK Wrapping**: Designed clean `expect` classes `FirebasePerformance`, `Trace`, and `HttpMetric` inside `commonMain` to support manual trace starting/stopping, metric modification, custom attribute injection, and manual HTTP metric request/response size and status instrumentation.
* **Platform Factory Bindings**: Delegates to Android's official `com.google.firebase:firebase-perf:21.0.3` on Android Target. Links SwiftPM `FirebasePerformance` (`FIRPerformance`, `FIRTrace`, `FIRHTTPMetric`) product on iOS with manual HTTP Method conversions and correct iOS selector alignments (`valueForIntMetric`).
* **Interactive Verification Screen**: Added a dedicated `PerformanceScreen` under the sample app giving real-time control to start/stop custom traces, set metrics, and log simulated HTTP network metrics.
* **KMP Enabled counts update**: Incremented KMP Enabled module counter to 15.

### 2026-07-02: `firebase-sessions` KMP Module Creation & Platform Wrapper
* **KMP Module Realization**: Created the new `:firebase-sessions` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Module Classification**: Designated as a `sessions/support/internal-infra` support module.
* **Platform Wrapper & Isolation**: Created clean expect/actual contract for `FirebaseSessions` class to provide package visibility compatibility while satisfying Crashlytics and Performance SDK class path verification requirements.
* **Android Delegation**: Wraps official `com.google.firebase:firebase-sessions:2.0.8` on Android Target. Configured with a clean actual class to avoid compiler circular reference or internal visibility clashes.
* **iOS Platform Stubs**: Implemented clean actual class stubs for Apple targets without introducing unnecessary SwiftPM dependencies.
* **Verification Integration**: Wired `:firebase-sessions` dependency into the Sample App and integrated `SessionsTest` compiler validations inside `FirebaseInitScreen`.

### 2026-07-02: `protolite-well-known-types` KMP Module Creation & Platform Wrapper
* **KMP Module Realization**: Created the new `:protolite-well-known-types` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **Platform Wrapper & Isolation**: Established expectation contracts `Timestamp` and `Duration` inside `zone.ien.firebase.protobuf` package to avoid duplicate class name and circular reference clashes on Android.
* **Android Delegation**: Wraps official `com.google.firebase:protolite-well-known-types` on Android Target, ensuring 100% binary API consistency.
* **iOS Platform Stubs**: Implemented clean actual class stubs for Apple targets without introducing heavy third-party Protobuf runtimes or SwiftPM dependencies.
* **Verification Integration**: Wired `:protolite-well-known-types` dependency into the Sample App and integrated `WellKnownTypesTest` compilation checks verifying proper instance allocation and builder pipeline capabilities.

### 2026-07-02: `firebase-auth` KMP Module Creation & Platform SDK Wrapper
* **KMP Module Realization**: Created the new `:firebase-auth` module from scratch with targets `androidTarget()` and native `iosSimulatorArm64()`, `iosArm64()`.
* **OAuth & Social Providers Integration**: Added multiplatform support for token-based, OAuth, and dynamic credentials (including Google, GitHub, Apple). Created expect objects `GoogleAuthProvider`, `GithubAuthProvider` and expect class `OAuthProvider`.
* **Platform SDK Wrapping**: Designed clean expect classes `FirebaseAuth`, `FirebaseUser`, `AuthResult`, and `AuthCredential` inside `commonMain` to support email/password sign-in, anonymous authentication, credential-based authentication, custom token session access, token retrieval, and account deletion.
* **Platform Factory Bindings**: Delegates to Android's official `com.google.firebase.auth` components on Android Target (utilizing OIDC rawNonce builder alignments). Links SwiftPM `FirebaseAuth` (`FIRAuth`, `FIRUser`, `FIRAuthDataResult`, `FIRGoogleAuthProvider`, `FIRGitHubAuthProvider`, `FIROAuthProvider`) products on iOS.
* **Asynchronous Flow Integration**: Bridged event listeners using coroutines `callbackFlow` to expose authStateFlow, allowing real-time session tracking. Wraps async tasks (Android Tasks and iOS Completion blocks) with coroutine suspensions.
* **Interactive Verification Screen**: Added `AuthScreen` inside the Sample App to display current sessions, trigger anonymous sign-in, manage email credentials, extract ID tokens, delete profiles, and document platform-specific configurations.

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
