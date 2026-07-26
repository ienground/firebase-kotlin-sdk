// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_inappmessaging_display",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_inappmessaging_display",
      type: .none,
      targets: ["_firebase_inappmessaging_display"]
    )
  ],
  dependencies: [
    .package(
      url: "https://github.com/firebase/firebase-ios-sdk.git",
      from: "12.14.0"
    )
  ],
  targets: [
    .target(
      name: "_firebase_inappmessaging_display",
      dependencies: [
        .product(
          name: "FirebaseInAppMessaging-Beta",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
