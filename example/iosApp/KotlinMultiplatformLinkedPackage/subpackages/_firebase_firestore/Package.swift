// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_firestore",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_firestore",
      type: .none,
      targets: ["_firebase_firestore"]
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
      name: "_firebase_firestore",
      dependencies: [
        .product(
          name: "FirebaseFirestore",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
