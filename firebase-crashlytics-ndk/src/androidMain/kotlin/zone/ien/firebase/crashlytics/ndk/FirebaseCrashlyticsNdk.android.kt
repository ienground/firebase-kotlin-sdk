package zone.ien.firebase.crashlytics.ndk

public actual class FirebaseCrashlyticsNdk private constructor() {

    public actual fun isNdkCrashCaptureEnabled(): Boolean {
        // NDK support class is present and integrated into compilation classpath
        return true
    }

    public actual companion object {
        private val instance = FirebaseCrashlyticsNdk()
        public actual fun getInstance(): FirebaseCrashlyticsNdk = instance
    }
}
