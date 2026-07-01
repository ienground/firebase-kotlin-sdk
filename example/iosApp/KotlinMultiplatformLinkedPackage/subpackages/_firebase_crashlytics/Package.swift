// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_crashlytics",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_crashlytics",
      type: .none,
      targets: ["_firebase_crashlytics"]
    )
  ],
  dependencies: [
    .package(
      url: "https://github.com/firebase/firebase-ios-sdk.git",
      from: "11.3.0"
    )
  ],
  targets: [
    .target(
      name: "_firebase_crashlytics",
      dependencies: [
        .product(
          name: "FirebaseCrashlytics",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
