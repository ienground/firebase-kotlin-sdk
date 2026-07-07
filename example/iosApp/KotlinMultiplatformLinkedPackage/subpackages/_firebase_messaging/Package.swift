// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_messaging",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_messaging",
      type: .none,
      targets: ["_firebase_messaging"]
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
      name: "_firebase_messaging",
      dependencies: [
        .product(
          name: "FirebaseMessaging",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
