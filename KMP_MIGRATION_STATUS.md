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

### 2026-11-16: 의존성 갱신 및 Apple SDK 13 호환 마이그레이션
* **버전**: Firebase Android BoM 35.2.0, Apple SDK 13.1.1, Compose 1.12.2, Navigation3 1.2.0, ienlab-cmp 3.8.0, HIG 27.2.6으로 갱신했습니다. AGP, Kotlin, Gradle, Android SDK 설정 및 라이브러리 자체 버전은 유지했습니다.
* **버전 선택**: 안정 버전을 우선하며, 안정 릴리스가 없는 AI On-Device는 beta08로 갱신했습니다. Material3는 Compose 1.12.2 및 공통 UI 라이브러리의 배포 의존성과 일치하는 1.12.0-alpha03을 사용합니다. 최신 시험 버전 1.13.0-beta02는 Compose 전체를 시험 버전으로 올려야 하므로 제외했습니다.
* **샘플 앱**: 제거된 IenText를 기존 구현과 동일한 Material3 Text로 교체하고, deprecated 구분선을 IenDivider.Horizontal로 변경했습니다. IenIcon은 새 IconData.Vector 인자를 사용합니다. Navigation3 구조와 다크 모드는 유지했습니다.
* **Messaging (Type B)**: iOS actual에 isInstallationIdEnabled 및 register()/unregister()를 추가해 공식 FID 등록 API에 위임합니다. FCM 토큰과 FID의 의미가 다르므로 기존 공통 토큰 API 및 샘플의 토큰 모드는 유지합니다. FID 모드의 제한과 네이티브 delegate 연결 지점을 README 두 문서에 기록했습니다.
* **Swift-only 제약**: Apple SDK 13에서 제거된 FirebaseMLModelDownloader와 FirebaseAI 제품을 새로 링크하지 않습니다. 기존 ML Model Downloader, AI Logic, Data Connect의 iOS 메모리 actual 제약을 유지합니다. 네이티브 AI 연동에는 FirebaseAILogic 제품을 사용해야 합니다.
* **검증**: 카탈로그의 버전이 지정된 라이브러리 65개의 POM 존재 및 새 공통 UI 소스의 사용 API를 확인했습니다. 저장소의 Firebase SwiftPM 제품 14개를 담은 독립 임시 패키지에서 `swift package resolve`가 성공했고, 결과의 pins로 샘플 Xcode 잠금 파일을 갱신했습니다. 생성된 로컬 Kotlin SwiftPM 패키지의 Package.swift가 현재 없어 Xcode/Gradle 통합 resolve 및 빌드·테스트·기기 실행은 수행하지 않았습니다.

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
