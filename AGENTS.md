# AGENTS.md

## Firebase KMP Migration Rules
- Never copy source from firebase-android-sdk or firebase-ios-sdk.
- Use library dependencies only.
- Android must use Gradle/Maven artifacts.
- iOS must use SwiftPM if needed.
- Official repositories are reference-only.
- Keep KMP wrappers thin.
- Prefer commonMain for shared APIs.
- Reuse sample app architecture: Navigation3, dark mode, home grid.
- Do not over-engineer annotation-only modules.