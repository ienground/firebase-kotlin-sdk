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
    .package(path: "subpackages/_firebase_firestore"),
    .package(path: "subpackages/_firebase_storage"),
    .package(path: "subpackages/_firebase_database"),
    .package(path: "subpackages/_firebase_crashlytics"),
    .package(path: "subpackages/_firebase_functions"),
    .package(path: "subpackages/_appcheck_firebase_appcheck_debug"),
    .package(path: "subpackages/_appcheck_firebase_appcheck"),
    .package(path: "subpackages/_appcheck_firebase_appcheck_interop"),
    .package(path: "subpackages/_firebase_common")
  ],
  targets: [
    .target(
      name: "KotlinMultiplatformLinkedPackage",
      dependencies: [
        .product(name: "_firebase_firestore", package: "_firebase_firestore"),
        .product(name: "_firebase_storage", package: "_firebase_storage"),
        .product(name: "_firebase_database", package: "_firebase_database"),
        .product(name: "_firebase_crashlytics", package: "_firebase_crashlytics"),
        .product(name: "_firebase_functions", package: "_firebase_functions"),
        .product(name: "_appcheck_firebase_appcheck_debug", package: "_appcheck_firebase_appcheck_debug"),
        .product(name: "_appcheck_firebase_appcheck", package: "_appcheck_firebase_appcheck"),
        .product(name: "_appcheck_firebase_appcheck_interop", package: "_appcheck_firebase_appcheck_interop"),
        .product(name: "_firebase_common", package: "_firebase_common")
      ]
    )
  ]
)
