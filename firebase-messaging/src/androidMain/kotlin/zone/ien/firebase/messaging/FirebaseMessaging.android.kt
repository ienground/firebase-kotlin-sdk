package zone.ien.firebase.messaging

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging as AndroidFirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService as AndroidFirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage as AndroidRemoteMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import zone.ien.firebase.FirebaseApp
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

public actual class FirebaseMessaging private constructor(
    private val delegate: AndroidFirebaseMessaging,
    private val appIdentity: String
) {
    public constructor(delegate: AndroidFirebaseMessaging) : this(
        delegate = delegate,
        appIdentity = DEFAULT_MESSAGING_APP_IDENTITY
    )

    public actual var isAutoInitEnabled: Boolean
        get() = delegate.isAutoInitEnabled
        set(value) {
            delegate.isAutoInitEnabled = value
        }

    public actual var isDeliveryMetricsExportToBigQueryEnabled: Boolean
        get() = delegate.deliveryMetricsExportToBigQueryEnabled()
        set(value) {
            delegate.setDeliveryMetricsExportToBigQuery(value)
        }

    public actual val messages: Flow<RemoteMessage>
        get() = MessagingEventDispatcher.messages

    public actual val tokenUpdates: Flow<String>
        get() = seededTokenUpdates(
            currentToken = { delegate.token.await() },
            events = MessagingEventDispatcher.tokenEvents(appIdentity)
        )

    public actual suspend fun getToken(): String? = delegate.token.await()

    public actual suspend fun deleteToken() {
        delegate.deleteToken().await()
    }

    public actual suspend fun subscribeToTopic(topic: String) {
        delegate.subscribeToTopic(topic).await()
    }

    public actual suspend fun unsubscribeFromTopic(topic: String) {
        delegate.unsubscribeFromTopic(topic).await()
    }

    public actual fun handleMessage(message: RemoteMessage): MessageDeliveryResult =
        MessagingEventDispatcher.emitMessage(message)

    public actual companion object {
        public actual fun getInstance(): FirebaseMessaging {
            return FirebaseMessaging(
                delegate = AndroidFirebaseMessaging.getInstance(),
                appIdentity = DEFAULT_MESSAGING_APP_IDENTITY
            )
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessaging {
            // Android SDK's getInstance(FirebaseApp) is package-private.
            // We retrieve the component instance via FirebaseApp component container get() lookup.
            val messaging = app.androidApp.get(AndroidFirebaseMessaging::class.java)
            return FirebaseMessaging(messaging, app.getName())
        }
    }
}

/**
 * 공통 Flow로 FCM 콜백을 전달하는 선택형 서비스입니다.
 *
 * 앱의 `res/values`에서 `firebase_messaging_service_enabled`를 `true`로 오버라이드할 때만
 * 활성화됩니다. 기존 서비스를 유지하는 앱은 [FirebaseMessagingServiceBridge]를 사용하십시오.
 */
public open class FirebaseMessagingService : AndroidFirebaseMessagingService() {
    override fun onMessageReceived(message: AndroidRemoteMessage) {
        when (FirebaseMessagingServiceBridge.handleMessage(message)) {
            MessageDeliveryResult.ENQUEUED -> Unit
            MessageDeliveryResult.DROPPED_OLDEST -> Log.w(TAG, "FCM 메시지 버퍼가 가득 차 가장 오래된 메시지를 제거했습니다.")
            MessageDeliveryResult.FAILED -> Log.e(TAG, "FCM 메시지를 공통 흐름에 전달하지 못했습니다.")
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        FirebaseMessagingServiceBridge.handleNewToken(token)
    }

    private companion object {
        const val TAG = "FirebaseMessaging"
    }
}

/** 기존 Android [AndroidFirebaseMessagingService]에서 공통 Flow로 이벤트를 전달합니다. */
public object FirebaseMessagingServiceBridge {
    public fun handleMessage(message: AndroidRemoteMessage): MessageDeliveryResult =
        MessagingEventDispatcher.emitMessage(message.toFirebaseRemoteMessage())

    public fun handleNewToken(token: String) {
        MessagingEventDispatcher.emitToken(DEFAULT_MESSAGING_APP_IDENTITY, token)
    }
}

/** Android FCM 메시지를 공통 모델이 지원하는 필드로 변환합니다. */
public fun AndroidRemoteMessage.toFirebaseRemoteMessage(): RemoteMessage = RemoteMessage(
    data = data,
    notification = notification?.let { Notification(title = it.title, body = it.body) },
    from = from,
    messageId = messageId,
    messageType = messageType,
    collapseKey = collapseKey,
    sentTime = sentTime,
    ttl = ttl,
    priority = priority.toCommonPriority(),
    originalPriority = originalPriority.toCommonPriority()
)

private fun Int.toCommonPriority(): MessagePriority = when (this) {
    AndroidRemoteMessage.PRIORITY_HIGH -> MessagePriority.HIGH
    AndroidRemoteMessage.PRIORITY_NORMAL -> MessagePriority.NORMAL
    else -> MessagePriority.UNKNOWN
}

// Coroutines helper to await any GMS Task without play-services dependency errors
private suspend fun <T> Task<T>.awaitTask(): T? = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            cont.resume(task.result)
        } else {
            val exception = task.exception ?: Exception("FCM Task failed with unknown exception")
            cont.resumeWithException(exception)
        }
    }
}
