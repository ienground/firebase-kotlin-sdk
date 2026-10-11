package zone.ien.firebase.crashlytics.ndk

public actual class FirebaseCrashlyticsNdk private constructor() {

    public actual fun isNdkCrashCaptureEnabled(): Boolean {
        return false
    }

    public actual companion object {
        private val instance = FirebaseCrashlyticsNdk()
        public actual fun getInstance(): FirebaseCrashlyticsNdk = instance
    }
}
