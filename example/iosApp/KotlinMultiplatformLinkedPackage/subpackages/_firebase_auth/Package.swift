// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_auth",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_auth",
      type: .none,
      targets: ["_firebase_auth"]
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
      name: "_firebase_auth",
      dependencies: [
        .product(
          name: "FirebaseAuth",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
