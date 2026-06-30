package zone.ien.firebase.messaging

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import zone.ien.firebase.FirebaseApp
import swiftPMImport.zone.ien.firebase.firebase.messaging.FIRMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseMessaging private constructor(private val iosMessaging: FIRMessaging) {
    actual suspend fun getToken(): String = suspendCancellableCoroutine { cont ->
        iosMessaging.tokenWithCompletion { token, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (token != null) {
                cont.resume(token)
            } else {
                cont.resumeWithException(RuntimeException("FCM Token result is null"))
            }
        }
    }

    actual suspend fun deleteToken(): Unit = suspendCancellableCoroutine { cont ->
        iosMessaging.deleteTokenWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual fun addOnMessageReceivedListener(listener: (Map<String, String>) -> Unit) {
        listeners.add(listener)
    }

    actual companion object {
        private val listeners = mutableListOf<(Map<String, String>) -> Unit>()

        fun notifyMessageReceived(data: Map<String, String>) {
            listeners.forEach { it(data) }
        }

        actual fun getInstance(): FirebaseMessaging {
            return FirebaseMessaging(FIRMessaging.messaging())
        }

        actual fun getInstance(app: FirebaseApp): FirebaseMessaging {
            // Note: SwiftPM FIRMessaging maps to messaging() instance which implicitly binds default app.
            // Custom App bindings can fallback or use internal initializers.
            return FirebaseMessaging(FIRMessaging.messaging())
        }
    }
}
