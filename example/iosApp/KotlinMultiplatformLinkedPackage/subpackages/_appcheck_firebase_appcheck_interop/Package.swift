// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_appcheck_firebase_appcheck_interop",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_appcheck_firebase_appcheck_interop",
      type: .none,
      targets: ["_appcheck_firebase_appcheck_interop"]
    )
  ],
  dependencies: [
    .package(
      url: "https://github.com/firebase/firebase-ios-sdk.git",
      from: "12.15.0"
    )
  ],
  targets: [
    .target(
      name: "_appcheck_firebase_appcheck_interop",
      dependencies: [
        .product(
          name: "FirebaseAppCheck",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
