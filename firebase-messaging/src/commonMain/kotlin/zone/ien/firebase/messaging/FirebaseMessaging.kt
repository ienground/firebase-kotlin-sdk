package zone.ien.firebase.messaging

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import zone.ien.firebase.Firebase
import zone.ien.firebase.FirebaseApp

public expect class FirebaseMessaging {
    public var isAutoInitEnabled: Boolean
    public var isDeliveryMetricsExportToBigQueryEnabled: Boolean
    /**
     * 플랫폼 통합이 전달한 메시지를 수신합니다.
     *
     * 수집자가 없거나 느린 동안 최근 64개를 보존하며, 용량을 초과하면 가장 오래된 메시지를
     * 제거합니다. 브리지 호출자는 [MessageDeliveryResult]로 제거 여부를 확인할 수 있습니다.
     * 버퍼는 프로세스 전역이며 모든 [FirebaseMessaging] 인스턴스가 공유합니다.
     */
    public val messages: Flow<RemoteMessage>

    /**
     * FCM 토큰 변경을 수신합니다.
     *
     * 마지막 갱신 값을 재전달하고, 수집을 시작할 때 플랫폼 SDK에서 현재 토큰을 조회합니다.
     * iOS에서는 수집하는 동안 토큰 갱신 notification만 관찰하며 `FIRMessaging.delegate`를 변경하지
     * 않습니다. Android replay state는 Firebase app identity별로 분리되며, iOS에서는 기본 Firebase
     * app의 토큰만 나타냅니다.
     */
    public val tokenUpdates: Flow<String>

    public suspend fun getToken(): String?
    public suspend fun deleteToken()
    public suspend fun subscribeToTopic(topic: String)
    public suspend fun unsubscribeFromTopic(topic: String)
    /**
     * 플랫폼이 전달한 메시지를 [messages]로 전달합니다.
     *
     * iOS 호스트는 원격 알림 app delegate 콜백에서 이 함수를 호출해야 합니다.
     */
    public fun handleMessage(message: RemoteMessage): MessageDeliveryResult

    public companion object {
        public fun getInstance(): FirebaseMessaging

        /** iOS에서는 Apple SDK 제약으로 현재 기본 `FIRMessaging` 인스턴스를 반환합니다. */
        public fun getInstance(app: FirebaseApp): FirebaseMessaging
    }
}

public class Notification(
    public val title: String?,
    public val body: String?
)

public class RemoteMessage(
    public val data: Map<String, String>,
    public val notification: Notification?,
    public val from: String?,
    public val messageId: String?,
    public val messageType: String?,
    public val collapseKey: String?,
    public val sentTime: Long,
    public val ttl: Int,
    public val priority: MessagePriority,
    public val originalPriority: MessagePriority
) {
    public constructor(data: Map<String, String>, notification: Notification?) : this(
        data = data,
        notification = notification,
        from = null,
        messageId = null,
        messageType = null,
        collapseKey = null,
        sentTime = 0L,
        ttl = 0,
        priority = MessagePriority.UNKNOWN,
        originalPriority = MessagePriority.UNKNOWN
    )
}

public enum class MessagePriority {
    UNKNOWN,
    HIGH,
    NORMAL
}

/** 메시지를 bounded 공통 버퍼에 전달한 결과입니다. */
public enum class MessageDeliveryResult {
    ENQUEUED,
    DROPPED_OLDEST,
    FAILED
}

internal class MessagingEventBuffer(messageCapacity: Int) {
    private val messageCapacity = messageCapacity.also {
        require(it > 0) { "메시지 버퍼 용량은 1 이상이어야 합니다." }
    }
    private val messageEvents = MutableSharedFlow<RemoteMessage>(
        replay = messageCapacity,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val messageCount = MutableStateFlow(0)
    private val tokenState = MutableStateFlow(TokenSnapshot(token = null, version = 0L))

    val messages: Flow<RemoteMessage> = messageEvents.asSharedFlow()
    val tokenUpdates: Flow<String> = tokenState
        .map { it.token }
        .filterNotNull()
        .distinctUntilChanged()

    fun emitMessage(message: RemoteMessage): MessageDeliveryResult {
        val dropsOldest = reserveMessageSlot()
        if (!messageEvents.tryEmit(message)) return MessageDeliveryResult.FAILED
        return if (dropsOldest) {
            MessageDeliveryResult.DROPPED_OLDEST
        } else {
            MessageDeliveryResult.ENQUEUED
        }
    }

    fun emitToken(token: String) {
        while (true) {
            val current = tokenState.value
            val next = TokenSnapshot(token = token, version = current.version + 1)
            if (tokenState.compareAndSet(current, next)) return
        }
    }

    fun tokenSnapshot(): TokenSnapshot = tokenState.value

    fun seedTokenIfUnchanged(snapshot: TokenSnapshot, token: String) {
        tokenState.compareAndSet(
            expect = snapshot,
            update = TokenSnapshot(token = token, version = snapshot.version + 1)
        )
    }

    private fun reserveMessageSlot(): Boolean {
        while (true) {
            val current = messageCount.value
            val next = minOf(current + 1, messageCapacity)
            if (messageCount.compareAndSet(current, next)) return current >= messageCapacity
        }
    }
}

internal data class TokenSnapshot(val token: String?, val version: Long)

internal const val DEFAULT_MESSAGING_APP_IDENTITY: String = "[DEFAULT]"

internal class MessagingTokenEventRegistry {
    private val entries = MutableStateFlow<Map<String, MessagingEventBuffer>>(emptyMap())

    fun eventsFor(appIdentity: String): MessagingEventBuffer {
        while (true) {
            val current = entries.value
            current[appIdentity]?.let { return it }

            val created = MessagingEventBuffer(messageCapacity = 1)
            if (entries.compareAndSet(current, current + (appIdentity to created))) {
                return created
            }
        }
    }
}

internal object MessagingEventDispatcher {
    private val messageEvents = MessagingEventBuffer(messageCapacity = 64)
    private val tokenEventRegistry = MessagingTokenEventRegistry()

    val events: MessagingEventBuffer
        get() = tokenEvents(DEFAULT_MESSAGING_APP_IDENTITY)

    val messages: Flow<RemoteMessage> = messageEvents.messages
    val tokenUpdates: Flow<String> = events.tokenUpdates

    fun emitMessage(message: RemoteMessage): MessageDeliveryResult = messageEvents.emitMessage(message)

    fun tokenEvents(appIdentity: String): MessagingEventBuffer =
        tokenEventRegistry.eventsFor(appIdentity)

    fun emitToken(token: String) {
        emitToken(DEFAULT_MESSAGING_APP_IDENTITY, token)
    }

    fun emitToken(appIdentity: String, token: String) {
        tokenEvents(appIdentity).emitToken(token)
    }
}

internal fun seededTokenUpdates(
    currentToken: suspend () -> String?,
    events: MessagingEventBuffer
): Flow<String> = flow {
    val snapshot = events.tokenSnapshot()
    val token = try {
        currentToken()
    } catch (exception: CancellationException) {
        throw exception
    } catch (_: Exception) {
        null
    }
    token?.let { events.seedTokenIfUnchanged(snapshot, it) }
    emitAll(events.tokenUpdates)
}.distinctUntilChanged()

internal fun remoteMessageFromApplePayload(payload: Map<String, Any?>): RemoteMessage {
    val aps = payload["aps"] as? Map<*, *>
    val alert = aps?.get("alert")
    val notification = when (alert) {
        is String -> Notification(title = null, body = alert)
        is Map<*, *> -> Notification(
            title = alert["title"] as? String,
            body = alert["body"] as? String
        )
        else -> null
    }
    val reservedKeys = setOf("aps", "from", "message_type", "collapse_key")
    val data = payload.mapNotNull { (key, value) ->
        val isReserved = key in reservedKeys || key.startsWith("google.") || key.startsWith("gcm.")
        if (!isReserved && value is String) key to value else null
    }.toMap()

    return RemoteMessage(
        data = data,
        notification = notification,
        from = payload["from"] as? String,
        messageId = (payload["gcm.message_id"] ?: payload["google.message_id"]) as? String,
        messageType = payload["message_type"] as? String,
        collapseKey = (payload["collapse_key"] ?: payload["gcm.collapse_key"]) as? String,
        sentTime = payload["google.c.a.ts"].asLongOrNull()?.times(1_000) ?: 0L,
        ttl = payload["gcm.ttl"].asLongOrNull()?.toInt() ?: 0,
        priority = MessagePriority.UNKNOWN,
        originalPriority = MessagePriority.UNKNOWN
    )
}

private fun Any?.asLongOrNull(): Long? = when (this) {
    is Number -> toLong()
    is String -> toLongOrNull()
    else -> null
}

public val Firebase.messaging: FirebaseMessaging
    get() = FirebaseMessaging.getInstance()

public fun Firebase.messaging(app: FirebaseApp): FirebaseMessaging =
    FirebaseMessaging.getInstance(app)
