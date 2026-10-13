package zone.ien.firebase.crashlytics.ndk

import zone.ien.firebase.Firebase

public expect class FirebaseCrashlyticsNdk {
    public fun isNdkCrashCaptureEnabled(): Boolean

    public companion object {
        public fun getInstance(): FirebaseCrashlyticsNdk
    }
}

public val Firebase.crashlyticsNdk: FirebaseCrashlyticsNdk
    get() = FirebaseCrashlyticsNdk.getInstance()
