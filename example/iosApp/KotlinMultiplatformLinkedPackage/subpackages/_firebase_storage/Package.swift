// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_storage",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_storage",
      type: .none,
      targets: ["_firebase_storage"]
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
      name: "_firebase_storage",
      dependencies: [
        .product(
          name: "FirebaseStorage",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
