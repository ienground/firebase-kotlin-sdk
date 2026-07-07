// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_perf",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_perf",
      type: .none,
      targets: ["_firebase_perf"]
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
      name: "_firebase_perf",
      dependencies: [
        .product(
          name: "FirebasePerformance",
          package: "firebase-ios-sdk"
        )
      ]
    )
  ]
)
