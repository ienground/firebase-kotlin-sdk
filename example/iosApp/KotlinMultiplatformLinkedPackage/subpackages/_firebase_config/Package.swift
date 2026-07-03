// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_config",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_config",
      type: .none,
      targets: ["_firebase_config"]
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
      name: "_firebase_config",
      dependencies: [
        .product(
          name: "FirebaseRemoteConfig",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
