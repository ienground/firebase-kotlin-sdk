package zone.ien.firebase.crashlytics

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey
import swiftPMImport.zone.ien.firebase.firebase.crashlytics.FIRCrashlytics
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseCrashlytics private constructor(
    private val iosCrashlytics: FIRCrashlytics
) {
    public actual fun log(message: String) {
        iosCrashlytics.log(message)
    }

    public actual fun recordException(throwable: Throwable) {
        val domain = throwable::class.qualifiedName ?: throwable::class.simpleName ?: "KotlinException"
        val stackTrace = throwable.stackTraceToString()
        val code = stackTrace.hashCode().toLong()
        val error = NSError.errorWithDomain(
            domain = domain,
            code = code,
            userInfo = mapOf(
                NSLocalizedDescriptionKey to (throwable.message ?: "Unknown Kotlin Exception"),
                "KotlinStackTrace" to stackTrace
            )
        )
        iosCrashlytics.recordError(error)
    }

    public actual fun setCustomKey(key: String, value: String) {
        iosCrashlytics.setCustomValue(value, key)
    }

    public actual fun setCustomKey(key: String, value: Boolean) {
        iosCrashlytics.setCustomValue(platform.Foundation.NSNumber(bool = value), key)
    }

    public actual fun setCustomKey(key: String, value: Double) {
        iosCrashlytics.setCustomValue(platform.Foundation.NSNumber(double = value), key)
    }

    public actual fun setCustomKey(key: String, value: Float) {
        iosCrashlytics.setCustomValue(platform.Foundation.NSNumber(float = value), key)
    }

    public actual fun setCustomKey(key: String, value: Int) {
        iosCrashlytics.setCustomValue(platform.Foundation.NSNumber(int = value), key)
    }

    public actual fun setCustomKey(key: String, value: Long) {
        iosCrashlytics.setCustomValue(platform.Foundation.NSNumber(longLong = value), key)
    }

    public actual fun setUserId(identifier: String) {
        iosCrashlytics.setUserID(identifier)
    }

    public actual fun setCrashlyticsCollectionEnabled(enabled: Boolean) {
        iosCrashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    public actual fun deleteUnsentReports() {
        iosCrashlytics.deleteUnsentReports()
    }

    public actual fun sendUnsentReports() {
        iosCrashlytics.sendUnsentReports()
    }

    public actual suspend fun checkForUnsentReports(): Boolean = suspendCancellableCoroutine { cont ->
        iosCrashlytics.checkForUnsentReportsWithCompletion { hasUnsentReports ->
            cont.resume(hasUnsentReports)
        }
    }

    public actual fun didCrashOnPreviousExecution(): Boolean {
        return false
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseCrashlytics {
            val shared = FIRCrashlytics.crashlytics()
            return FirebaseCrashlytics(shared)
        }
    }
}
