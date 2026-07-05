// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_appdistribution",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_appdistribution",
      type: .none,
      targets: ["_firebase_appdistribution"]
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
      name: "_firebase_appdistribution",
      dependencies: [
        .product(
          name: "FirebaseAppDistribution-Beta",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
