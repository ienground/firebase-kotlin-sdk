# Firebase Kotlin SDK (한국어)

[English](README.md) | **한국어**


[![Kotlin](https://img.shields.io/badge/kotlin-2.4.0-blue.svg)](https://kotlinlang.org)
[![Platform](https://img.shields.io/badge/platform-android%20%7C%20ios-lightgrey.svg)](https://kotlinlang.org/docs/multiplatform.html)

공식 Firebase 플랫폼 SDK들을 Kotlin Multiplatform(KMP) 환경에서 자연스럽게 사용할 수 있도록 감싸 안은, Kotlin-first 설계 기반의 Android 및 iOS용 래퍼 라이브러리입니다.

---

## 주요 특징

- **Kotlin-First 설계**: Coroutines(`suspend` 함수) 및 비동기 데이터 스트림 처리를 위한 `Flow`를 완벽히 네이티브하게 지원합니다.
- **가볍고 무결한 어댑터 구조**: 복잡한 추상화 레이어를 걷어내고, 안드로이드의 공식 GMS(Maven) 의존성과 iOS의 공식 SwiftPM 의존성에 직접 작업을 위임(Delegate)하도록 설계되었습니다.
- **안정적인 플랫폼 가드**: iOS 컴파일 제약으로 인해 지원되지 않는 기능들의 경우, 빌드 시점에 에러를 발생시키는 대신 런타임에 명확한 설명이 기재된 예외(Stub)로 안전하게 대체 처리합니다.

### 서비스 지원 현황 매트릭스

| Firebase 기능 명칭 | Android 지원 | iOS 지원 | 완성도 | 내부 구현 메커니즘 |
| :--- | :---: | :---: | :---: | :--- |
| **Authentication** (`firebase-auth`) | 🟢 지원 | 🟢 지원 | **95%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Cloud Firestore** (`firebase-firestore`) | 🟢 지원 | 🟢 지원 | **90%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Realtime Database** (`firebase-database`) | 🟢 지원 | 🟢 지원 | **85%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Cloud Storage** (`firebase-storage`) | 🟢 지원 | 🟢 지원 | **90%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Cloud Functions** (`firebase-functions`) | 🟢 지원 | 🟢 지원 | **95%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Remote Config** (`firebase-config`) | 🟢 지원 | 🟢 지원 | **90%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Crashlytics** (`firebase-crashlytics`) | 🟢 지원 | 🟢 지원 | **90%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Cloud Messaging** (`firebase-messaging`) | 🟢 지원 | 🟢 지원 | **85%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Performance Monitoring** (`firebase-perf`) | 🟢 지원 | 🟢 지원 | **80%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Installations** (`firebase-installations`) | 🟢 지원 | 🟢 지원 | **95%** | Native GMS / iOS SwiftPM SDK 위임 |
| **App Check** (`firebase-appcheck`) | 🟢 지원 | 🟢 지원 | **90%** | Native GMS / iOS SwiftPM SDK 위임 |
| **A/B Testing** (`firebase-abt`) | 🟢 지원 | 🟢 지원 | **95%** | Native GMS / iOS SwiftPM SDK 위임 |
| **Sessions** (`firebase-sessions`) | 🟢 지원 | 🔴 Stub | **20%** (iOS Stub) | KMP wrapper (iOS stub) |
| **Encoders & Decoders** (`firebase-encoders`) | 🟢 지원 | 🟢 지원 | **95%** | Pure Kotlin 직렬화 파이프라인 |
| **Model Downloader** (`firebase-ml-modeldownloader`)| 🟢 지원 | 🔴 Stub | **10%** (iOS Stub) | Swift 전용 바이너리 제약으로 iOS는 Stub 대체 |
| **AI Logic (Gemini Cloud)** (`firebase-ai`) | 🟢 지원 | 🔴 Stub | **15%** (iOS Stub) | Swift 전용 바이너리 제약으로 iOS는 Stub 대체 |
| **AI On-Device (Gemini Nano)** (`firebase-ai-ondevice`)| 🟢 지원 | 🔴 Stub | **15%** (iOS Stub) | Swift 전용 바이너리 제약으로 iOS는 Stub 대체 |
| **App Distribution** (`firebase-appdistribution`) | 🟢 지원 | 🔴 Stub | **20%** (iOS Stub) | iOS 플랫폼 제한으로 Stub 대체 |
| **Data Connect (GraphQL)** (`firebase-dataconnect`) | 🟢 지원 | 🔴 Stub | **10%** (iOS Stub) | Swift 전용 바이너리 제약으로 iOS는 Stub 대체 |
| **In-App Messaging** (`firebase-inappmessaging`) | 🟢 지원 | 🔴 Stub | **10%** (iOS Stub) | Swift 전용 바이너리 제약으로 iOS는 Stub 대체 |

---

## 설치 방법

공통 Kotlin Multiplatform 모듈의 `build.gradle.kts` 의존성 블록에 필요한 아티팩트를 추가합니다:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Core Common Firebase 기본 모듈
            implementation("zone.ien.firebase:firebase-common:0.9.0")
            
            // 사용하고자 하는 기능 래퍼 추가
            implementation("zone.ien.firebase:firebase-auth:0.9.0")
            implementation("zone.ien.firebase:firebase-firestore:0.9.0")
        }
    }
}
```

### 플랫폼별 추가 설정

#### Android
루트 및 앱 수준의 `build.gradle.kts`에 `google-services` 플러그인을 적용하고, `google-services.json` 파일을 올바른 경로에 배치합니다.

#### iOS (SwiftPM 연동)
본 라이브러리는 Apple 타겟 빌드 시 Xcode Swift Package Manager를 통한 네이티브 링킹을 지원합니다. iOS 빌드 파이프라인에서 의존성이 정상 해결되도록 해소 명령을 함께 실행해 줍니다:
```bash
# iOS 빌드 단계 수행 전 SPM 의존성 리졸브 실행
xcodebuild -resolvePackageDependencies -workspace iosApp.xcworkspace -scheme iosApp
```

> [!IMPORTANT]
> **최소 Kotlin 버전 요구사항**: 본 라이브러리의 SwiftPM 연동 및 가져오기 기법은 **Kotlin 2.4.0**부터 정식 도입된 새로운 `swiftPMDependencies` DSL 기능을 기반으로 구현되었습니다. 따라서, iOS 타겟을 컴파일하고 링킹하기 위해서는 **Kotlin 2.4.0 이상의 컴파일러 버전이 필수로 요구**됩니다.


---

## 사용 예시

### SDK 초기화
플랫폼 고유 컨텍스트를 주입하여 Firebase 인스턴스를 초기화합니다:

```kotlin
import zone.ien.firebase.Firebase
import zone.ien.firebase.initialize

// Firebase Core 초기화
val app = Firebase.initialize(context) // FirebasePlatformContext
```

### Firestore 데이터 읽기/쓰기
코틀린 멀티플랫폼 공통 소스셋에서 직접 Firestore 문서를 조작합니다:

```kotlin
import zone.ien.firebase.firestore.firestore
import zone.ien.firebase.Firebase

val db = Firebase.firestore
val document = db.collection("users").document("user_id")

// 비동기 방식으로 데이터 쓰기
document.set(mapOf("name" to "홍길동", "age" to 30))

// Kotlin Coroutines 기반으로 데이터 가져오기 (Awaiting)
val snapshot = document.get()
val userName = snapshot.get<String>("name")
```

---

## 마이그레이션 가이드 (Migration Guide)

### 마이그레이션 대상
1. 안드로이드 전용 네이티브 **GMS Firebase SDK**에서 Kotlin Multiplatform으로 환경을 전환하려는 개발자.
2. 기존 KMP 라이브러리(예: **GitLive의 firebase-kotlin-sdk**)에서 본 라이브러리로 이관하여 iOS 미지원 플랫폼에 대한 예외 가드 처리를 확보하려는 개발 팀.

### 패키지 명칭(Namespace) 매핑 표

가이드에 따라 기존 코드의 임포트(Import) 명세를 변경해 줍니다:

| 대상 컴포넌트 | 공식 Android Native SDK | GitLive SDK | 본 SDK 패키지 경로 |
| :--- | :--- | :--- | :--- |
| **Core App** | `com.google.firebase.Firebase` | `dev.gitlive.firebase.Firebase` | `zone.ien.firebase.Firebase` |
| **Auth** | `com.google.firebase.auth.FirebaseAuth` | `dev.gitlive.firebase.auth.auth` | `zone.ien.firebase.auth.auth` |
| **Firestore** | `com.google.firebase.firestore.FirebaseFirestore`| `dev.gitlive.firebase.firestore.firestore`| `zone.ien.firebase.firestore.firestore` |
| **Remote Config** | `com.google.firebase.remoteconfig.FirebaseRemoteConfig`| `dev.gitlive.firebase.config.config`| `zone.ien.firebase.config.config` |

### 주요 API 동작상 변화

- **동기/비동기 태스크 매핑**: Android의 `Task<T>` 나 iOS의 비동기 콜백 패턴은 모두 코틀린 표준 `suspend` 함수로 래핑되어 리턴 값을 직접 받도록 단일화되었습니다.
- **실시간 이벤트 관찰 (Observers)**: 모든 실시간 변경 리스너는 코틀린의 `Flow<T>` 스트림으로 노출됩니다. 기존의 콜백 등록 코드를 코루틴 스코프 내의 `.collect { ... }` 로직으로 전환하십시오.
- **iOS 미지원 Stub 예외**: `firebase-inappmessaging` 이나 `firebase-dataconnect` 와 같이 iOS 타겟에서 컴파일이 불가능한 모듈들의 경우, 컴파일 시 빌드 에러를 유발하는 대신 런타임에 호출 시 `UnsupportedOperationException`을 발생시켜 공통 소스셋 컴파일 형상을 깨뜨리지 않도록 조정했습니다.

---

## 플랫폼 제약사항 및 주의사항

### iOS 내 Swift 전용 모듈 컴파일 이슈
구글 공식 iOS SDK의 인앱 메시지, Gemini AI, Data Connect 모듈은 Objective-C 호환 헤더가 없는 순수 Swift로 구현되어 있어 Kotlin/Native의 cinterop 도구(`convertSyntheticImportProjectIntoDefFile`)로 직접 결합할 수 없는 한계가 존재합니다.
따라서 본 래퍼 라이브러리 상에서도 해당 기능의 iOS 타겟은 동작 시 `UnsupportedOperationException` 예외가 발생하므로 사용 시 주의가 필요합니다.

**대처 가이드**: 공통 소스셋이나 프리젠테이션 레이어에서 플랫폼 구별 플래그를 통해 호출 코드를 안전하게 보호해 주십시오:
```kotlin
import zone.ien.firebase.example.util.isIos

if (!isIos) {
    // 안드로이드에서 정상 지원되는 AI 생성 로직 실행
    Firebase.ai.generativeModel("gemini-3.5-flash").generateContent(prompt)
} else {
    // iOS에서는 지원되지 않음을 안내하는 UI 처리
}
```

---

## 라이선스 (License)

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
