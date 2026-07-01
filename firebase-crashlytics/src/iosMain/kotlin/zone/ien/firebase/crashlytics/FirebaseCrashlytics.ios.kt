package zone.ien.firebase.crashlytics

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey
import swiftPMImport.zone.ien.firebase.firebase.crashlytics.FIRCrashlytics

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseCrashlytics private constructor(
    private val iosCrashlytics: FIRCrashlytics
) {
    public actual fun log(message: String) {
        iosCrashlytics.log(message)
    }

    public actual fun recordException(throwable: Throwable) {
        val error = NSError.errorWithDomain(
            domain = "zone.ien.firebase.crashlytics",
            code = 0,
            userInfo = mapOf(NSLocalizedDescriptionKey to (throwable.message ?: "Unknown Kotlin Exception"))
        )
        iosCrashlytics.recordError(error)
    }

    public actual fun setCustomKey(key: String, value: String) {
        iosCrashlytics.setCustomValue(value, key)
    }

    public actual fun setCustomKey(key: String, value: Boolean) {
        iosCrashlytics.setCustomValue(value, key)
    }

    public actual fun setCustomKey(key: String, value: Double) {
        iosCrashlytics.setCustomValue(value, key)
    }

    public actual fun setCustomKey(key: String, value: Float) {
        iosCrashlytics.setCustomValue(value, key)
    }

    public actual fun setCustomKey(key: String, value: Int) {
        iosCrashlytics.setCustomValue(value, key)
    }

    public actual fun setCustomKey(key: String, value: Long) {
        iosCrashlytics.setCustomValue(value, key)
    }

    public actual fun setUserId(identifier: String) {
        iosCrashlytics.setUserID(identifier)
    }

    public actual fun setCrashlyticsCollectionEnabled(enabled: Boolean) {
        iosCrashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseCrashlytics {
            val shared = FIRCrashlytics.crashlytics()
            return FirebaseCrashlytics(shared)
        }
    }
}
