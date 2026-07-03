# Firebase Android SDK - Kotlin Multiplatform (KMP) Migration Status

This document tracks the KMP migration status across all subprojects defined in the repository.

---

## 📊 Migration Summary

- **Total SDKs**: 36
- **KMP Enabled**: 19
- **Android Native Only**: 17

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
| `firebase-auth`                                       | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS SwiftPM). |
| `firebase-common`                                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-components`                                 | `sdk` | 🟢 Migrated |  Android, iOS     | KMP common components.   |
| `firebase-components:firebase-dynamic-module-support` | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS stub).  |
| `firebase-config`                                     | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Remote Config wrapper (iOS SwiftPM). |
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
| `firebase-installations-interop`                      | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS stub).  |
| `firebase-installations`                              | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Firebase Installations SDK wrapper. |
| `firebase-ml-modeldownloader`                         | `sdk` | 🟢 Migrated |  Android, iOS     | KMP wrapper (iOS Unsupported). |
| `firebase-perf`                                       | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Performance Monitoring wrapper. |
| `firebase-sessions`                                   | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Sessions internal support. |
| `firebase-storage`                                    | `sdk` | 🟢 Migrated |  Android, iOS     | KMP SwiftPM wrapper.     |
| `protolite-well-known-types`                          | `sdk` | 🟢 Migrated |  Android, iOS     | KMP Protobuf Well-Known Types wrapper. |
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
