package zone.ien.firebase.crashlytics.ndk

public expect class FirebaseCrashlyticsNdk {
    public fun isNdkCrashCaptureEnabled(): Boolean

    public companion object {
        public fun getInstance(): FirebaseCrashlyticsNdk
    }
}
