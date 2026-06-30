// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_functions",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_functions",
      type: .none,
      targets: ["_firebase_functions"]
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
      name: "_firebase_functions",
      dependencies: [
        .product(
          name: "FirebaseFunctions",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
