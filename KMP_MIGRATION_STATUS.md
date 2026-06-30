# Firebase Android SDK - Kotlin Multiplatform (KMP) Migration Status

This document tracks the KMP migration status across all subprojects defined in the repository.

---

## 📊 Migration Summary

- **Total SDKs**: 35
- **KMP Enabled**: 0
- **Android Native Only**: 35

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
| `firebase-abt`                                        | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-annotations`                                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-appdistribution`                            | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-appdistribution-api`                        | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-common`                                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-components`                                 | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-components:firebase-dynamic-module-support` | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-config`                                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-config-interop`                             | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-crashlytics`                                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-crashlytics-ndk`                            | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-database`                                   | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-database-collection`                        | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-dataconnect`                                | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-dataconnect:connectors`                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-datatransport`                              | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-functions`                                  | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-messaging`                                  | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-messaging-directboot`                       | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-inappmessaging`                             | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-inappmessaging-display`                     | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-installations-interop`                      | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-installations`                              | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-ml-modeldownloader`                         | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-perf`                                       | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-sessions`                                   | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
| `firebase-storage`                                    | `sdk` | 🔴 Pending  |   Android Only    | Native Android SDK only. |
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
