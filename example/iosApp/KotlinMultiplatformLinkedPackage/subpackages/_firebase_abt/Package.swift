// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_firebase_abt",
  platforms: [
    .iOS("15.0")
  ],
  products: [
    .library(
      name: "_firebase_abt",
      type: .none,
      targets: ["_firebase_abt"]
    )
  ],
  dependencies: [],
  targets: [
    .target(
      name: "_firebase_abt",
      dependencies: []
    )
  ]
)
