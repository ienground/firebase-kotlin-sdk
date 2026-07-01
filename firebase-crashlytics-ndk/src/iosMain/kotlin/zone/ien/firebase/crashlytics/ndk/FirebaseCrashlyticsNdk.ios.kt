package zone.ien.firebase.crashlytics.ndk

public actual class FirebaseCrashlyticsNdk private constructor() {

    public actual fun isNdkCrashCaptureEnabled(): Boolean {
        throw UnsupportedOperationException("FirebaseCrashlyticsNdk is only supported on Android. Apple platforms do not use NDK for crash recording.")
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseCrashlyticsNdk {
            throw UnsupportedOperationException("FirebaseCrashlyticsNdk is only supported on Android. Apple platforms do not use NDK for crash recording.")
        }
    }
}
