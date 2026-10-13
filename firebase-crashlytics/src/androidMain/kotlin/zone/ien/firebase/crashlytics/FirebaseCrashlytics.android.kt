package zone.ien.firebase.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics as AndroidFirebaseCrashlytics
import kotlinx.coroutines.tasks.await

public actual class FirebaseCrashlytics private constructor(
    private val androidCrashlytics: AndroidFirebaseCrashlytics
) {
    public actual fun log(message: String) {
        androidCrashlytics.log(message)
    }

    public actual fun recordException(throwable: Throwable) {
        androidCrashlytics.recordException(throwable)
    }

    public actual fun setCustomKey(key: String, value: String) {
        androidCrashlytics.setCustomKey(key, value)
    }

    public actual fun setCustomKey(key: String, value: Boolean) {
        androidCrashlytics.setCustomKey(key, value)
    }

    public actual fun setCustomKey(key: String, value: Double) {
        androidCrashlytics.setCustomKey(key, value)
    }

    public actual fun setCustomKey(key: String, value: Float) {
        androidCrashlytics.setCustomKey(key, value)
    }

    public actual fun setCustomKey(key: String, value: Int) {
        androidCrashlytics.setCustomKey(key, value)
    }

    public actual fun setCustomKey(key: String, value: Long) {
        androidCrashlytics.setCustomKey(key, value)
    }

    public actual fun setUserId(identifier: String) {
        androidCrashlytics.setUserId(identifier)
    }

    public actual fun setCrashlyticsCollectionEnabled(enabled: Boolean) {
        androidCrashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    public actual fun deleteUnsentReports() {
        androidCrashlytics.deleteUnsentReports()
    }

    public actual fun sendUnsentReports() {
        androidCrashlytics.sendUnsentReports()
    }

    public actual suspend fun checkForUnsentReports(): Boolean {
        return androidCrashlytics.checkForUnsentReports().await()
    }

    public actual fun didCrashOnPreviousExecution(): Boolean {
        return androidCrashlytics.didCrashOnPreviousExecution()
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseCrashlytics {
            return FirebaseCrashlytics(AndroidFirebaseCrashlytics.getInstance())
        }
    }
}
