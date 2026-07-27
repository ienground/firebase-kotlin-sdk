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
| `firebase-firestore`                                  | `sdk` | 🟢 Migrated | Android, iOS      | KMP wrapper (iOS SwiftPM), FieldPath/value mapping (null values), snapshot APIs (`getSnapshots`), `whereInArray`/`inArray`, WriteBatch, Transaction, AggregateQuery (count/sum/average), Settings, Bundle (loadBundle/namedQuery), DocumentChange, and mergeFields. |
| `firebase-auth`                                       | `sdk` | 🟢 Migrated | Android, iOS      | User authentication, state listeners, provider credentials, and expanded FirebaseUser properties (`displayName`, `photoUrl`, `phoneNumber`, `isEmailVerified`). |
| `firebase-storage`                                    | `sdk` | 🟢 Migrated | Android, iOS      | File upload/download tasks, metadata, and StorageReference.        |
| `firebase-database`                                   | `sdk` | 🟢 Migrated | Android, iOS      | Realtime Database references, snapshots, children iteration, and expanded query filters (`startAt`, `endAt`). |
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

### 2026-07-26: Complete Cross-Module API Parity Audit & Implementation Expansion
* **Firebase Auth**: Added `displayName`, `photoUrl`, `phoneNumber`, and `isEmailVerified` properties to `FirebaseUser` with Android (`FirebaseUser.android.kt`) and iOS (`FirebaseUser.ios.kt`) actual mappings.
* **Firebase Realtime Database**: Added `childrenCount`, `children: Iterable<DataSnapshot>`, `hasChildren()` to `DataSnapshot`, and `startAt`, `endAt` filters across String, Double, and Boolean overloads to `Query`.
* **Firebase Firestore**: Full parity complete with `Map<String, Any?>` null mapping, `DocumentChange`, `QuerySnapshot.documentChanges`/`documents`, `getSnapshots()`, `inArray`/`whereInArray`, `mergeFields`, `WriteBatch`, `Transaction`, `AggregateQuery` (`count`/`sum`/`average`), `Settings`, `Bundle`, and `FieldValue` property accessors (`serverTimestamp`, `delete`).
* **Verification**: Verified Android host tests (`BUILD SUCCESSFUL`) and iOS Simulator compilation (`BUILD SUCCESSFUL`).

### 2026-07-27: GitLive SDK Migration Compatibility Enhancements (IEN-39)
* **Firebase Storage**: Added `FirebaseStorage.reference(location: String)` extension function delegating to `getReference(location)`.
* **Firebase Firestore**: Added `typealias Direction = QueryDirection`, `DocumentSnapshot.exists`/`id` extension properties, generic `DocumentSnapshot.get<T>()`, `DocumentReference.collection(path)`, `Query.snapshots`/`DocumentReference.snapshots` property accessors, `QueryFilterBuilder` DSL (`greaterThanOrEqualTo`, `lessThanOrEqualTo`, `arrayContainsAny`, `whereIn`, `whereNotIn`), and `Timestamp.fromMilliseconds(Double)` / `Timestamp.fromMilliseconds(Long)` factory functions in `Timestamp.Companion`.
* **Firebase Auth**: Added `FirebaseAuth.authStateChanged` extension property delegating to `authStateFlow`, `EmailAuthProvider.credential` extension function delegating to `getCredential`, and `FirebaseUser.reauthenticateAndRetrieveData` extension function delegating to `reauthenticate`.
* **Firebase Functions**: Added `FirebaseFunctions.httpsCallable` extensions & `operator get`, `HttpsCallableReference.invoke(data)` operator, `HttpsCallableResult.data<T>()` generic function, and `FirebaseFunctionsException.effectiveCode` extension property delegating to `code`.
* **Tests**: Added unit tests in `commonTest` for all 4 modules verifying extension properties and factory methods.
* **Firebase Storage (gitlive alignment)**: Converted `putBytes`, `putData`, and `putFile` on `StorageReference` from returning `UploadTask` to `suspend` functions that internally await completion before returning, matching the gitlive SDK pattern.
