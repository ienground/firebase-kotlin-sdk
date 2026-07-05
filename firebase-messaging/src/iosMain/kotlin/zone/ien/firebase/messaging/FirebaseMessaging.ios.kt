package zone.ien.firebase.messaging

import swiftPMImport.zone.ien.firebase.firebase.messaging.FIRMessaging
import kotlinx.cinterop.ExperimentalForeignApi
import zone.ien.firebase.FirebaseApp
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseMessaging(private val delegate: FIRMessaging) {

    public actual var isAutoInitEnabled: Boolean
        get() = delegate.isAutoInitEnabled()
        set(value) {
            delegate.setAutoInitEnabled(value)
        }

    public actual suspend fun getToken(): String? = suspendCoroutine { cont ->
        delegate.tokenWithCompletion { token, error ->
            if (error != null) {
                cont.resumeWithException(Exception("FCM token fetch failed: ${error.localizedDescription}"))
            } else {
                cont.resume(token)
            }
        }
    }

    public actual suspend fun deleteToken(): Unit = suspendCoroutine { cont ->
        delegate.deleteTokenWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(Exception("FCM token deletion failed: ${error.localizedDescription}"))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual suspend fun subscribeToTopic(topic: String): Unit = suspendCoroutine { cont ->
        delegate.subscribeToTopic(topic) { error ->
            if (error != null) {
                cont.resumeWithException(Exception("FCM subscribe to topic failed: ${error.localizedDescription}"))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual suspend fun unsubscribeFromTopic(topic: String): Unit = suspendCoroutine { cont ->
        delegate.unsubscribeFromTopic(topic) { error ->
            if (error != null) {
                cont.resumeWithException(Exception("FCM unsubscribe from topic failed: ${error.localizedDescription}"))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseMessaging {
            return FirebaseMessaging(FIRMessaging.messaging())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessaging {
            // Apple SDK focuses on default messaging() instance but we route delegation gracefully.
            return FirebaseMessaging(FIRMessaging.messaging())
        }
    }
}
