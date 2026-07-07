// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_inappmessaging",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_inappmessaging",
      type: .none,
      targets: ["_firebase_inappmessaging"]
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
      name: "_firebase_inappmessaging",
      dependencies: [
        .product(
          name: "FirebaseInAppMessaging-Beta",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
