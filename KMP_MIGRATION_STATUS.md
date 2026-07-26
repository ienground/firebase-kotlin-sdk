# KMP Migration Status

This document tracks the Kotlin Multiplatform (KMP) migration status across all modules in this repository.

## Overview
- **Migration Strategy**: Wrap official Firebase platform SDKs (`firebase-android-sdk`, `firebase-ios-sdk`) with thin KMP facades.
- **Android Target**: Delegates to official Maven/Gradle Firebase SDK dependencies.
- **iOS Target**: Delegates to official Apple Firebase SDK dependencies linked via Swift Package Manager (`Package.swift`).

## Module Status Table

| Module Name                                           | Type  | Status      | Targets           | Details / Caveats                                                  |
| :---------------------------------------------------- | :---: | :---------: | :---------------- | :----------------------------------------------------------------- |
| `firebase-common`                                     | `sdk` | 🟢 Migrated | Android, iOS      | Core `Firebase` / `FirebaseApp` initialization and app registry.    |
| `firebase-components`                                 | `sdk` | 🟢 Migrated | Android, iOS      | Dependency injection & component container contracts.              |
| `firebase-annotations`                                | `sdk` | 🟢 Migrated | Common-only       | Marker annotations and common metadata types.                      |
| `firebase-firestore`                                  | `sdk` | 🟢 Migrated | Android, iOS      | KMP wrapper (iOS SwiftPM), FieldPath/value mapping, snapshot APIs, WriteBatch, Transaction, AggregateQuery (count/sum/average), Settings, Bundle (loadBundle/namedQuery). |
| `firebase-auth`                                       | `sdk` | 🟢 Migrated | Android, iOS      | User authentication, state listeners, and provider credentials.    |
| `firebase-storage`                                    | `sdk` | 🟢 Migrated | Android, iOS      | File upload/download tasks, metadata, and StorageReference.        |
| `firebase-database`                                   | `sdk` | 🟢 Migrated | Android, iOS      | Realtime Database references, snapshots, and query filters.        |
| `firebase-functions`                                  | `sdk` | 🟢 Migrated | Android, iOS      | HTTPS Callable functions & region endpoints.                        |
| `firebase-messaging`                                  | `sdk` | 🟢 Migrated | Android, iOS      | Token handling, message flows, and APNs integration bridge.        |
| `firebase-config`                                     | `sdk` | 🟢 Migrated | Android, iOS      | Remote Config fetch, activate, and parameter values.               |
| `firebase-crashlytics`                                | `sdk` | 🟢 Migrated | Android, iOS      | Exception logging, custom keys, and crash reporting.               |
| `firebase-performance`                                | `sdk` | 🟢 Migrated | Android, iOS      | Performance trace metrics and network monitoring.                  |
| `firebase-appcheck`                                   | `sdk` | 🟢 Migrated | Android, iOS      | App Check token providers & debug helpers.                         |
| `firebase-installations`                              | `sdk` | 🟢 Migrated | Android, iOS      | Installation ID and token management.                              |
| `firebase-installations-interop`                      | `sdk` | 🟢 Migrated | Android, iOS      | Interop interfaces for installations token access.                 |
| `firebase-abt`                                        | `sdk` | 🟢 Migrated | Android, iOS      | A/B Testing experiment state mapping and validation.               |
| `firebase-inappmessaging`                             | `sdk` | 🟢 Migrated | Android, iOS      | In-App Messaging trigger management and message flows.             |
| `firebase-inappmessaging-display`                     | `sdk` | 🟢 Migrated | Android, iOS      | Custom display delegate and typed display models.                  |
| `firebase-dataconnect`                                | `sdk` | 🟢 Migrated | Android, iOS      | Android official operation delegate; iOS memory actual.            |
| `firebase-dataconnect:connectors`                     | `sdk` | 🟢 Migrated | Android, iOS      | Connector descriptors and operations.                              |

---

## Detailed Migration Logs

### 2026-07-26: Firebase Firestore Batch, Transaction, Aggregate Query & Settings Implementation
* **WriteBatch**: Added multiplatform `WriteBatch` expect/actual class with `set(documentRef, data)`, `set(documentRef, data, merge)`, `update(documentRef, data)`, `delete(documentRef)`, and `commit()` suspend function delegating to native Android `WriteBatch` and iOS `FIRWriteBatch`.
* **Transaction**: Added `Transaction` expect/actual class supporting `get(documentRef)`, `set(documentRef, data)`, `set(documentRef, data, merge)`, `update(documentRef, data)`, and `delete(documentRef)` with `FirebaseFirestore.runTransaction { transaction -> ... }` suspend helper delegating to native transaction execution blocks on Android and iOS.
* **Aggregate Queries**: Added `AggregateField` (supporting `count()`, `sum()`, `average()`) and `AggregateQuerySnapshot` (`count`, `get`, `getLong`, `getDouble`) with `Query.count()`, `Query.sum()`, `Query.average()`, and `Query.aggregate()` delegating to native Android/iOS aggregation APIs.
* **Firestore Settings**: Added `FirebaseFirestoreSettings` expect/actual class and `firestoreSettings { ... }` builder DSL supporting `host`, `isSslEnabled`, and `isPersistenceEnabled` configuration along with `FirebaseFirestore.setSettings()` and `FirebaseFirestore.clearPersistence()`.
* **Firestore Bundles**: Added `loadBundle(bundleData: ByteArray): LoadBundleTaskProgress` and `namedQuery(name: String): Query?` on `FirebaseFirestore` supporting Firestore bundle loading and named query retrieval from cache.
* **Verification**: Added `FirestoreBatchAndTransactionTest` verifying settings builder DSL, `AggregateField` constructors, `LoadBundleTaskProgress` model, and `AggregateSource` enum. Verified Android host tests and iOS Simulator compilation.
