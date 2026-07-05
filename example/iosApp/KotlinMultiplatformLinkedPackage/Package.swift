// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "KotlinMultiplatformLinkedPackage",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "KotlinMultiplatformLinkedPackage",
      type: .none,
      targets: ["KotlinMultiplatformLinkedPackage"]
    )
  ],
  dependencies: [
    .package(
      url: "https://github.com/firebase/firebase-ios-sdk.git",
      from: "11.3.0"
    ),
    .package(path: "subpackages/_firebase_firestore"),
    .package(path: "subpackages/_firebase_storage"),
    .package(path: "subpackages/_firebase_perf"),
    .package(path: "subpackages/_firebase_installations"),
    .package(path: "subpackages/_firebase_database"),
    .package(path: "subpackages/_firebase_crashlytics"),
    .package(path: "subpackages/_firebase_auth"),
    .package(path: "subpackages/_firebase_functions"),
    .package(path: "subpackages/_appcheck_firebase_appcheck_debug"),
    .package(path: "subpackages/_appcheck_firebase_appcheck"),
    .package(path: "subpackages/_appcheck_firebase_appcheck_interop"),
    .package(path: "subpackages/_firebase_config"),
    .package(path: "subpackages/_firebase_appdistribution"),
    .package(path: "subpackages/_firebase_messaging"),
    .package(path: "subpackages/_firebase_common")
  ],
  targets: [
    .target(
      name: "KotlinMultiplatformLinkedPackage",
      dependencies: [
        .product(
          name: "FirebaseCore",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseFirestore",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseAuth",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseCrashlytics",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseDatabase",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseStorage",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseFunctions",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseAppCheck",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseRemoteConfig",
          package: "firebase-ios-sdk"
        ),
        .product(name: "_firebase_firestore", package: "_firebase_firestore"),
        .product(name: "_firebase_storage", package: "_firebase_storage"),
        .product(name: "_firebase_perf", package: "_firebase_perf"),
        .product(name: "_firebase_installations", package: "_firebase_installations"),
        .product(name: "_firebase_database", package: "_firebase_database"),
        .product(name: "_firebase_crashlytics", package: "_firebase_crashlytics"),
        .product(name: "_firebase_auth", package: "_firebase_auth"),
        .product(name: "_firebase_functions", package: "_firebase_functions"),
        .product(name: "_appcheck_firebase_appcheck_debug", package: "_appcheck_firebase_appcheck_debug"),
        .product(name: "_appcheck_firebase_appcheck", package: "_appcheck_firebase_appcheck"),
        .product(name: "_appcheck_firebase_appcheck_interop", package: "_appcheck_firebase_appcheck_interop"),
        .product(name: "_firebase_config", package: "_firebase_config"),
        .product(name: "_firebase_appdistribution", package: "_firebase_appdistribution"),
        .product(name: "_firebase_messaging", package: "_firebase_messaging"),
        .product(name: "_firebase_common", package: "_firebase_common")
      ]
    )
  ]
)
