// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_database",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_database",
      type: .none,
      targets: ["_firebase_database"]
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
      name: "_firebase_database",
      dependencies: [
        .product(
          name: "FirebaseDatabase",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
