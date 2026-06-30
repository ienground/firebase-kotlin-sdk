# AGENTS.md

## Purpose
This repository builds a Kotlin Multiplatform wrapper/facade around Firebase platform SDKs.
The goal is to expose practical KMP APIs while keeping platform integrations thin and maintainable.

## Core rules
- Never copy source code from `firebase-android-sdk` or `firebase-ios-sdk` into this repository.
- Use platform SDKs only through library dependencies.
- Android integrations must use Gradle/Maven artifacts.
- iOS integrations must use Swift Package Manager when native Apple Firebase SDK linkage is required.
- Official upstream repositories are reference-only, never vendor or mirror their source here.
- Prefer thin wrappers/adapters/facades over reimplementing upstream SDK internals.
- Avoid over-engineering. Build the minimum practical API surface first.

## Upstream references
- Firebase Android SDK: https://github.com/firebase/firebase-android-sdk
- Firebase Apple SDK: https://github.com/firebase/firebase-ios-sdk

Use these repositories only to inspect API shape, behavior, and module structure.
Do not copy files, packages, or internal implementations from them.

## KMP migration rules
When converting a module to Kotlin Multiplatform:
- Replace Android-only Kotlin plugin usage with `org.jetbrains.kotlin.multiplatform` where appropriate.
- Add Android target and iOS targets only if the module meaningfully supports them.
- Add SwiftPM dependencies only when an Apple native SDK is actually required.
- Prefer `src/commonMain/kotlin` for shared contracts and thin common APIs.
- Reuse existing local Android wrapper code where appropriate.
- `androidMain` may point to existing local `src/main/java` or `src/main/kotlin` only for code that belongs to this repository.
- Never interpret source-set reuse as permission to import upstream source trees.

## Module design guidance
Before implementing, classify the module:

### Type A: common-only or mostly common
Examples: annotations, marker APIs, simple value models, lightweight contracts.
Rules:
- Prefer a simple `commonMain` implementation.
- Keep `androidMain` / `iosMain` minimal or empty if possible.
- Do not introduce expect/actual unless actually needed.

### Type B: wrapper around platform SDKs
Examples: runtime Firebase features that depend on Android/iOS SDK behavior.
Rules:
- Keep common APIs minimal and stable.
- Android actual implementations should delegate to official Firebase Android dependencies.
- iOS actual implementations should delegate to official Firebase Apple dependencies when supported.
- If iOS support is not practical yet, say so clearly and leave a minimal TODO/unsupported path instead of inventing behavior.

## Sample app rules
There is a sample/test app in this repository and it must stay usable.
When changing modules:
- Preserve existing sample app architecture.
- Keep Navigation3-based structure intact if already present.
- Keep dark mode support intact.
- Do not rebuild the sample app architecture unless required.
- Reflect new module integration in the sample app or test code only as much as needed to prove the module is wired correctly.
- For non-UI modules, lightweight integration proof is enough; avoid unnecessary demo screens.

## Implementation boundaries
- Do not add large abstractions "for the future" unless the current module clearly needs them.
- Do not duplicate upstream package internals.
- Do not port entire Java/Swift/Obj-C implementations into this repo.
- Do not add SwiftPM if the module does not need Apple native SDK linkage.
- Do not force iOS parity if the underlying feature is Android-specific or not ready yet.

## Decision policy
When unsure, prefer:
1. dependency over copied source
2. thin wrapper over deep abstraction
3. common contract over duplicated platform logic
4. explicit unsupported/TODO over fake cross-platform parity
5. small safe migration over broad refactor

## Expected task workflow
For each migration task:
1. Inspect current module structure.
2. Classify the module type.
3. Decide the smallest valid KMP migration shape.
4. Update Gradle/plugin/source-set configuration.
5. Add or adjust commonMain/androidMain/iosMain only as needed.
6. Integrate with sample app or tests at the minimum useful level.
7. Report what was changed, what was intentionally not changed, and what remains.

## Output expectations
For migration tasks, responses should usually include:
- Summary of changes
- Module classification
- File-by-file diffs or concrete code edits
- Source-set structure after migration
- SwiftPM necessity decision
- Sample app or test integration notes
- Remaining follow-up work

## Coding style preferences
- Prefer Kotlin DSL.
- Keep code and Gradle scripts concise.
- Match existing repository conventions before introducing new patterns.
- Minimize comments unless they explain a non-obvious constraint.
- Be explicit when something is intentionally unsupported.

## User preferences
- Respond in Korean unless asked otherwise.
- The user is a Kotlin and Compose Multiplatform mobile developer.
- Be direct, practical, and avoid inventing requirements not stated by the user.

## If context is missing
If a task is ambiguous:
- Do not guess hidden architecture.
- State the assumption briefly and choose the safest minimal implementation.
- If a critical choice cannot be inferred from the repo, ask.

## Hard stop conditions
Stop and call out the issue instead of guessing if:
- the requested approach requires copying upstream SDK source
- the module cannot be reasonably supported on iOS yet
- the sample app would need unnecessary large restructuring
- the requested migration conflicts with existing repository-wide KMP conventions

## Build and verification rules
- Do not run `./gradlew assemble`, `./gradlew build`, or other full-repository production build commands unless the user explicitly asks for it.
- The user runs full builds manually.
- Prefer lightweight verification only:
    - file-level reasoning
    - module-scoped checks only when truly necessary
    - existing tests only when small and directly relevant
- If build verification is needed, propose the command to the user instead of running it automatically.
- Do not trigger long Gradle tasks just to “be safe”.

## Migration status update rule
- After every completed KMP migration task, update `KMP_MIGRATION_STATUS.md`.
- Treat `KMP_MIGRATION_STATUS.md` as the migration progress tracker for this repository.
- Record what module was migrated, what was changed, what remains, and any important caveats or follow-up work.
- If the migration result is partial, clearly mark the incomplete areas.
- If sample app or test integration was updated, include that too.
- Do not skip this step after finishing a migration-related code change.

## Git staging rule
- If you create new files as part of the task, stage them with `git add .`.
- This applies especially to files newly created by the agent, such as source files, Gradle files, documentation updates, and migration status files.
- Do not assume newly created files will be picked up later; explicitly stage them with `git add .`.