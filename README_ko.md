<p align="center">
  <img src="images/firebase-kmp.png" alt="Firebase Kotlin SDK Logo" width="150" />
</p>

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
| **Sessions** (`firebase-sessions`) | 🟢 지원 | 🟢 지원 | **95%** | iOS SwiftPM SDK 링킹 완료 (백그라운드 세션 텔레메트리 자동 동작) |
| **Encoders & Decoders** (`firebase-encoders`) | 🟢 지원 | 🟢 지원 | **95%** | Pure Kotlin 직렬화 파이프라인 |
| **Model Downloader** (`firebase-ml-modeldownloader`)| 🟢 지원 | 🟡 부분 지원 | **80%** (iOS Partial) | iOS 메모리 기반 모델 조회/삭제 시뮬레이션 지원 (네이티브 링킹 미지원) |
| **AI Logic (Gemini Cloud)** (`firebase-ai`) | 🟢 지원 | 🟡 부분 지원 | **80%** (iOS Partial) | iOS 메모리 기반 가상 Gemini 응답 시뮬레이터 지원 (네이티브 링킹 미지원) |
| **AI On-Device (Gemini Nano)** (`firebase-ai-ondevice`)| 🟢 지원 | 🟡 부분 지원 | **80%** (iOS Partial) | iOS 메모리 기반 가상 온디바이스/하이브리드 AI 추론 시뮬레이션 지원 (네이티브 링킹 미지원) |
| **App Distribution** (`firebase-appdistribution`) | 🟢 지원 | 🟡 부분 지원 | **80%** (iOS Partial) | iOS 테스터 로그인 및 업데이트 확인 지원 (진행률 추적 미지원) |
| **Data Connect (GraphQL)** (`firebase-dataconnect`) | 🟢 지원 | 🟡 부분 지원 | **80%** (iOS Partial) | iOS 메모리 기반 메타데이터 Actual 지원 (네이티브 링킹 미지원) |
| **In-App Messaging** (`firebase-inappmessaging`) | 🟢 지원 | 🟢 지원 | **90%** | Native GMS / iOS SwiftPM SDK 위임 (Core 기능 실제 구현) |
| **In-App Messaging Display** (`firebase-inappmessaging-display`) | 🟢 지원 | 🟡 부분 지원 | **80%** (iOS Partial) | iOS 메모리 기반 가상 리스너 등록 시뮬레이션 지원 (네이티브 링킹 미지원) |

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

## 샘플 앱 실행 방법 (Running the Sample App)

이 저장소에는 [example](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/firebase-kotlin-sdk/example) 디렉터리에 위치한 Kotlin Multiplatform Compose 샘플 애플리케이션이 포함되어 있습니다.

샘플 앱을 성공적으로 빌드하고 직접 테스트해 보려면, 본인의 Firebase 설정 파일을 추가해야 합니다:
1. **안드로이드 (Android)**: 본인의 `google-services.json` 파일을 `example/androidApp/` 디렉터리 내부에 추가하십시오.
2. **iOS**: 본인의 `GoogleService-Info.plist` 파일을 `example/iosApp/` 프로젝트 내부(일반적으로 `iosApp/` 폴더 내부 및 Xcode 프로젝트 리소스 등록)에 추가한 뒤 빌드하십시오.

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
- **iOS 미지원 Stub 예외 해소 (Memory-based Actual)**: cinterop 제약으로 인해 iOS 바이너리가 직접 링크되지 못하는 일부 모듈들(AI Logic, Data Connect, ML Model Downloader 등)에 대해서도 런타임 크래시(`UnsupportedOperationException`)를 원천적으로 제거하고, 메모리 수준에서 인스턴스 획득 및 A/B 테스팅 구독 상태를 저장/반환하는 "메모리 보존 실제 인스턴스(Memory-based Actual)"로 전환하여 공통 컴파일 형상과 호출 안정성을 지켰습니다.

---

## 플랫폼 제약사항 및 주의사항

구글 공식 iOS SDK의 Gemini AI, Data Connect, Custom Model Downloader 및 In-App Messaging Custom Display 관련 컴포넌트는 Objective-C 호환 헤더가 없는 순수 Swift로 구현되어 있어 Kotlin/Native의 cinterop 도구(`convertSyntheticImportProjectIntoDefFile`)로 직접 결합할 수 없는 한계가 존재합니다.
따라서 본 래퍼 라이브러리 상에서도 해당 기능의 iOS 타겟은 가상 시뮬레이션 모드(Memory-based Actual)로 동작하여 런타임 크래시 없이 리스너 등록 및 로컬 조회 기능을 수행하나, 실제 원격 서비스 및 네이티브 동작은 iOS Swift 영역에서 직접 네이티브 SDK를 연동해 처리해야 합니다.
(※ In-App Messaging 모듈의 경우 Core 제어 API는 iOS 상에서 정상 작동하나, 네이티브 디스플레이 카드 UI 레이아웃의 직접 커스터마이징 제약은 존재합니다.)

### App Distribution iOS 연동 주의사항

1. **테스터 인증 리다이렉션 (URL Scheme 설정 필수)**:
   iOS에서 App Distribution을 통한 테스터 로그인을 성공적으로 마친 후 앱으로 복귀하기 위해서는, `GoogleService-Info.plist`의 `REVERSED_CLIENT_ID` (예: `com.googleusercontent.apps.628868686373-kg0v5qsu2ucsfdablu1k2gdi15o529em`) 값을 `Info.plist` 내 URL Schemes로 필수 등록해 주어야 합니다.
2. **App Delegate Swizzling 비활성화 대응**:
   만약 `FirebaseAppDelegateProxyEnabled`를 `false`로 설정하여 자동 Swizzling을 꺼둔 앱 환경이라면, `AppDelegate`의 `application(_:open:options:)` 메소드에서 `AppDistribution.appDistribution().handle(url)`을 수동으로 호출하여 URL 처리를 직접 위임해야 합니다.
3. **In-App Update Progress Monitoring 미지원**:
   iOS Firebase SDK는 앱 내에서 다운로드 진행률(바이트 단위)을 관찰하는 스트림 API를 제공하지 않습니다. 따라서 `updateIfNewReleaseAvailable` API를 호출하면 `UnsupportedOperationException`이 발생하며, 대신 `checkForNewRelease` 시 새 빌드가 존재할 경우 노출되는 SDK 자체 내장 UI Alert 흐름을 활용하여 배포가 진행됩니다.

### Data Connect iOS 연동 제약사항

1. **Swift 전용 라이브러리 및 cinterop 제약**:
   Google 공식 iOS `FirebaseDataConnect` SDK는 Swift로만 구현되어 있으며, Objective-C 호환 헤더가 존재하지 않습니다. 이로 인해 Kotlin/Native의 cinterop 컴파일 도구가 이를 해석하지 못해 네이티브 바이너리 직접 링킹이 불가능합니다.
2. **KMP 내부 동작 (메모리 보존 모드)**:
   KMP 공통 코드 단에서 컴파일을 보장하고 런타임 크래시를 유발하지 않도록, iOS의 actual 구현체는 **"메모리 보존 모드(Memory-based Actual)"** 로 빌드됩니다. 인스턴스 생성(`getInstance`), 설정 조회(`config`), 에뮬레이터 세팅(`useEmulator`) 등의 기능은 iOS 상에서도 안전하게 상태값을 메모리에 보존하며 정상 실행됩니다.
3. **실제 GraphQL 네트워크 통신 처리**:
   iOS 실제 디바이스 및 시뮬레이터에서 서버 또는 로컬 에뮬레이터와 통신하려면, KMP 공통 코드 대신 iOS 네이티브 Swift 앱 영역에서 Firebase CLI로 자동 생성된 Swift SDK를 직접 가져와 데이터를 교환하고 화면에 렌더링해야 합니다.

### Sessions iOS 연동 및 세션 자동 추적

1. **바이너리 연동 및 인프라 구동**:
   Firebase Sessions SDK는 개발자가 코드에서 직접 제어하는 public API를 거의 노출하지 않는 내부 백그라운드 인프라(Internal-only telemetry SDK)입니다. 이번 마이그레이션을 통해 `FirebaseSessions` SwiftPM 제품이 iOS 타겟에 빌드 타임에 정상 링크되도록 구성되었습니다.
2. **KMP 역할 및 자동 연계**:
   `FirebaseSessions` expect/actual 매핑을 통해 공통 코드 단에서의 클래스패스 가시성을 보장하며, 세션 ID 및 라이프사이클 이벤트는 iOS 앱 백그라운드 구동 시 SDK 내부에서 자동으로 추적되어 Crashlytics 및 Performance Monitoring SDK와 자동 연동되어 동작합니다.

### Model Downloader iOS 연동 제약사항

1. **Swift 전용 라이브러리 및 cinterop 제약**:
   Google 공식 iOS `FirebaseMLModelDownloader` SDK는 Swift로만 구현되어 있으며, Objective-C 호환 헤더가 존재하지 않습니다. 이로 인해 Kotlin/Native의 cinterop 컴파일 도구가 이를 해석하지 못해 네이티브 바이너리 직접 링킹이 불가능합니다.
2. **KMP 내부 동작 (메모리 보존 모드)**:
   KMP 공통 코드 단에서 컴파일을 보장하고 런타임 크래시를 유발하지 않도록, iOS의 actual 구현체는 **"메모리 보존 모드(Memory-based Actual)"** 로 빌드됩니다. 모델 요청(`getModel`), 모델 목록 조회(`listDownloadedModels`), 모델 제거(`deleteDownloadedModel`) 기능은 iOS 상에서도 안전하게 로컬 메모리 리스트에 가상 모델을 보존하며 정상 실행됩니다.
3. **실제 모델 파일 다운로드 처리**:
   iOS 실제 디바이스 및 시뮬레이터에서 서버로부터 TFLite 모델 파일을 물리적으로 다운로드하려면, KMP 공통 코드 대신 iOS 네이티브 Swift 앱 영역에서 Firebase ML Swift SDK를 직접 호출하여 사용해야 합니다.

### AI Logic iOS 연동 제약사항

1. **Swift 전용 라이브러리 및 cinterop 제약**:
   Google 공식 iOS `FirebaseAILogic` SDK는 Swift로만 구현되어 있으며, Objective-C 호환 헤더가 존재하지 않습니다. 이로 인해 Kotlin/Native의 cinterop 컴파일 도구가 이를 해석하지 못해 네이티브 바이너리 직접 링킹이 불가능합니다.
2. **KMP 내부 동작 (메모리 보존 모드)**:
   KMP 공통 코드 단에서 컴파일을 보장하고 런타임 크래시를 유발하지 않도록, iOS의 actual 구현체는 **"메모리 보존 모드(Memory-based Actual)"** 로 빌드됩니다. `generativeModel` 획득 및 `generateContent(prompt)` 호출은 iOS 상에서도 안전하게 1.5초 지연(delay) 후 프롬프트를 담아낸 가상의 답변 텍스트를 돌려주는 모의 엔진(Mock Engine)으로 정상 실행됩니다.
3. **실제 Gemini Cloud 및 Vertex AI API 통신 처리**:
   iOS 실제 디바이스 및 시뮬레이터에서 Vertex AI 클라우드 백엔드와 통신하려면, KMP 공통 코드 대신 iOS 네이티브 Swift 앱 영역에서 Firebase AI Swift SDK를 직접 호출하여 사용해야 합니다.

### AI On-Device iOS 연동 제약사항

1. **Swift 전용 라이브러리 및 cinterop 제약**:
   Google 공식 iOS 온디바이스/하이브리드 AI SDK(Apple Intelligence 기반)는 Swift로만 구현되어 있으며, Objective-C 호환 헤더가 존재하지 않습니다. 이로 인해 Kotlin/Native의 cinterop 컴파일 도구가 이를 해석하지 못해 네이티브 바이너리 직접 링킹이 불가능합니다.
2. **KMP 내부 동작 (메모리 보존 모드)**:
   KMP 공통 코드 단에서 컴파일을 보장하고 런타임 크래시를 유발하지 않도록, iOS의 actual 구현체는 **"메모리 보존 모드(Memory-based Actual)"** 로 빌드됩니다. `OnDeviceConfig` 와 함께 `generativeModel`을 획득하고 `generateContent(prompt)`를 호출하면, iOS 상에서도 안전하게 추론 모드(PREFER_ON_DEVICE, PREFER_IN_CLOUD, ONLY_ON_DEVICE) 설정을 분석하여 가상의 시뮬레이션 결과를 돌려줍니다.
3. **실제 온디바이스(Apple Intelligence) AI 추론 처리**:
   iOS 실제 디바이스 및 시뮬레이터에서 Apple Intelligence 기반 온디바이스 추론 및 하이브리드 기능을 완전히 활용하려면, KMP 공통 코드 대신 iOS 네이티브 Swift 앱 영역에서 Firebase AI Swift SDK를 직접 호출하여 사용해야 합니다.

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
Copyright (c) 2026. Firebase Kotlin SDK project and open source contributors.
Copyright (c) 2026. IENGROUND of IENLAB.

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
