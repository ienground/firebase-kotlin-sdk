// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_installations",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_installations",
      type: .none,
      targets: ["_firebase_installations"]
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
      name: "_firebase_installations",
      dependencies: [
        .product(
          name: "FirebaseInstallations",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
