package zone.ien.firebase.messaging

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSNotificationCenter
import swiftPMImport.zone.ien.firebase.firebase.messaging.FIRMessaging
import swiftPMImport.zone.ien.firebase.firebase.messaging.FIRMessagingRegistrationTokenRefreshedNotification
import zone.ien.firebase.FirebaseApp
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseMessaging(private val delegate: FIRMessaging) {

    public actual var isAutoInitEnabled: Boolean
        get() = delegate.isAutoInitEnabled()
        set(value) {
            delegate.setAutoInitEnabled(value)
        }

    public actual var isDeliveryMetricsExportToBigQueryEnabled: Boolean
        get() = false
        set(value) {}

    public actual val messages: Flow<RemoteMessage>
        get() = MessagingEventDispatcher.messages

    public actual val tokenUpdates: Flow<String>
        get() = callbackFlow {
            val notificationCenter = NSNotificationCenter.defaultCenter
            val observer = notificationCenter.addObserverForName(
                name = FIRMessagingRegistrationTokenRefreshedNotification,
                `object` = null,
                queue = null
            ) { notification ->
                (notification?.`object` as? String)?.let {
                    MessagingEventDispatcher.emitToken(DEFAULT_MESSAGING_APP_IDENTITY, it)
                }
            }
            val updatesJob = launch {
                seededTokenUpdates(
                    currentToken = { getToken() },
                    events = MessagingEventDispatcher.tokenEvents(DEFAULT_MESSAGING_APP_IDENTITY)
                ).collect { trySend(it) }
            }
            awaitClose {
                updatesJob.cancel()
                notificationCenter.removeObserver(observer)
            }
        }.buffer(Channel.CONFLATED).distinctUntilChanged()

    public actual suspend fun getToken(): String? = suspendCancellableCoroutine { cont ->
        delegate.tokenWithCompletion { token, error ->
            if (cont.isActive) {
                if (error != null) {
                    cont.resumeWithException(Exception("FCM token fetch failed: ${error.localizedDescription}"))
                } else {
                    cont.resume(token)
                }
            }
        }
    }

    public actual suspend fun deleteToken(): Unit = suspendCancellableCoroutine { cont ->
        delegate.deleteTokenWithCompletion { error ->
            if (cont.isActive) {
                if (error != null) {
                    cont.resumeWithException(Exception("FCM token deletion failed: ${error.localizedDescription}"))
                } else {
                    cont.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun subscribeToTopic(topic: String): Unit = suspendCancellableCoroutine { cont ->
        delegate.subscribeToTopic(topic) { error ->
            if (cont.isActive) {
                if (error != null) {
                    cont.resumeWithException(Exception("FCM subscribe to topic failed: ${error.localizedDescription}"))
                } else {
                    cont.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun unsubscribeFromTopic(topic: String): Unit = suspendCancellableCoroutine { cont ->
        delegate.unsubscribeFromTopic(topic) { error ->
            if (cont.isActive) {
                if (error != null) {
                    cont.resumeWithException(Exception("FCM unsubscribe from topic failed: ${error.localizedDescription}"))
                } else {
                    cont.resume(Unit)
                }
            }
        }
    }

    public actual fun handleMessage(message: RemoteMessage): MessageDeliveryResult =
        MessagingEventDispatcher.emitMessage(message)

    public actual companion object {
        public actual fun getInstance(): FirebaseMessaging {
            return FirebaseMessaging(FIRMessaging.messaging())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessaging {
            // Apple SDK의 공개 messaging() API는 기본 app 인스턴스만 제공합니다.
            return FirebaseMessaging(FIRMessaging.messaging())
        }
    }
}

/**
 * APNs app delegate 또는 `UNUserNotificationCenter` 콜백의 `userInfo`를 공통 [RemoteMessage]로
 * 변환합니다.
 */
public fun remoteMessageFromUserInfo(userInfo: Map<Any?, *>): RemoteMessage =
    remoteMessageFromApplePayload(
        userInfo.mapNotNull { (key, value) ->
            (key as? String)?.let { it to value }
        }.toMap()
    )
