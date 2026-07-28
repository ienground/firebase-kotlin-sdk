package zone.ien.firebase.messaging

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFalse

class MessagingModelsTest {
    @Test
    fun testConstructsNotificationAndDataMessage() {
        val notification = Notification("제목", "본문")
        val message = RemoteMessage(mapOf("route" to "inbox"), notification)

        assertEquals("제목", message.notification?.title)
        assertEquals("본문", message.notification?.body)
        assertEquals("inbox", message.data["route"])
    }

    @Test
    fun testSupportsDataOnlyMessageWithoutNotification() {
        val message = RemoteMessage(emptyMap(), null)

        assertNull(message.notification)
        assertEquals(emptyMap(), message.data)
    }

    @Test
    fun testPreservesIncomingMessageDeliveryMetadata() {
        val message = RemoteMessage(
            data = mapOf("route" to "inbox"),
            notification = Notification("제목", "본문"),
            from = "/topics/news",
            messageId = "message-id",
            messageType = "chat",
            collapseKey = "inbox",
            sentTime = 1_710_000_000_000,
            ttl = 3_600,
            priority = MessagePriority.HIGH,
            originalPriority = MessagePriority.NORMAL
        )

        assertEquals("/topics/news", message.from)
        assertEquals("message-id", message.messageId)
        assertEquals("chat", message.messageType)
        assertEquals("inbox", message.collapseKey)
        assertEquals(1_710_000_000_000, message.sentTime)
        assertEquals(3_600, message.ttl)
        assertEquals(MessagePriority.HIGH, message.priority)
        assertEquals(MessagePriority.NORMAL, message.originalPriority)
    }

    @Test
    fun testEmitsIncomingMessageEventsAsFlow() = runBlocking {
        val expected = RemoteMessage(data = mapOf("route" to "inbox"), notification = null)
        val received = async(start = CoroutineStart.UNDISPATCHED) {
            MessagingEventDispatcher.messages.first()
        }

        MessagingEventDispatcher.emitMessage(expected)

        assertEquals("inbox", received.await().data["route"])
    }

    @Test
    fun testEmitsNewTokenEventsAsFlow() = runBlocking {
        val events = MessagingEventBuffer(messageCapacity = 1)
        val received = async(start = CoroutineStart.UNDISPATCHED) {
            events.tokenUpdates.first()
        }

        events.emitToken("new-token")

        assertEquals("new-token", received.await())
    }

    @Test
    fun testBuffersMessagesUpToCapacityWhenNoCollector() = runBlocking {
        val events = MessagingEventBuffer(messageCapacity = 2)

        assertEquals(
            MessageDeliveryResult.ENQUEUED,
            events.emitMessage(RemoteMessage(mapOf("id" to "1"), null))
        )
        assertEquals(
            MessageDeliveryResult.ENQUEUED,
            events.emitMessage(RemoteMessage(mapOf("id" to "2"), null))
        )
        assertEquals(
            MessageDeliveryResult.DROPPED_OLDEST,
            events.emitMessage(RemoteMessage(mapOf("id" to "3"), null))
        )

        assertEquals(listOf("2", "3"), events.messages.take(2).toList().map { it.data["id"] })
    }

    @Test
    fun testReplaysLastTokenUpdateToNewCollector() = runBlocking {
        val events = MessagingEventBuffer(messageCapacity = 1)

        events.emitToken("latest-token")

        assertEquals("latest-token", events.tokenUpdates.first())
    }

    @Test
    fun testQueriesCurrentTokenFirstWhenCollectingTokenFlow() = runBlocking {
        val events = MessagingEventBuffer(messageCapacity = 1)

        val token = seededTokenUpdates(
            currentToken = { "current-token" },
            events = events
        ).first()

        assertEquals("current-token", token)
    }

    @Test
    fun testContinuesReceivingUpdatesEvenIfCurrentTokenQueryFails() = runBlocking {
        val events = MessagingEventBuffer(messageCapacity = 1)
        val received = async(start = CoroutineStart.UNDISPATCHED) {
            seededTokenUpdates(
                currentToken = { error("token fetch failed") },
                events = events
            ).first()
        }

        events.emitToken("updated-token")

        assertEquals("updated-token", received.await())
    }

    @Test
    fun testTokenSeedReplacesPreviousReplayValue() = runBlocking {
        val events = MessagingEventBuffer(messageCapacity = 1)
        events.emitToken("stale-token")

        assertEquals(
            "current-token",
            seededTokenUpdates(
                currentToken = { "current-token" },
                events = events
            ).first()
        )

        assertEquals("current-token", events.tokenUpdates.first())
    }

    @Test
    fun APNs_userInfo를_공통_메시지로_변환한다() {
        val message = remoteMessageFromApplePayload(
            mapOf(
                "aps" to mapOf(
                    "alert" to mapOf("title" to "제목", "body" to "본문")
                ),
                "gcm.message_id" to "message-id",
                "from" to "/topics/news",
                "collapse_key" to "inbox",
                "gcm.ttl" to 3_600,
                "google.c.a.ts" to 1_710_000_000,
                "route" to "inbox"
            )
        )

        assertEquals("제목", message.notification?.title)
        assertEquals("본문", message.notification?.body)
        assertEquals("message-id", message.messageId)
        assertEquals("/topics/news", message.from)
        assertEquals("inbox", message.collapseKey)
        assertEquals(3_600, message.ttl)
        assertEquals(1_710_000_000_000, message.sentTime)
        assertEquals("inbox", message.data["route"])
    }

    @Test
    fun APNs_예약키를_data에서_제외하고_message_type을_매핑한다() {
        val message = remoteMessageFromApplePayload(
            mapOf(
                "aps" to emptyMap<String, Any?>(),
                "google.c.sender.id" to "sender",
                "gcm.n.e" to "1",
                "from" to "/topics/news",
                "message_type" to "chat",
                "collapse_key" to "inbox",
                "route" to "detail"
            )
        )

        assertEquals(mapOf("route" to "detail"), message.data)
        assertEquals("chat", message.messageType)
    }

    @Test
    fun token_상태는_app_identity별로_분리한다() = runBlocking {
        val registry = MessagingTokenEventRegistry()
        val appA = registry.eventsFor("app-a")
        val appB = registry.eventsFor("app-b")

        appA.emitToken("token-a")
        appB.emitToken("token-b")

        assertEquals("token-a", appA.tokenUpdates.first())
        assertEquals("token-b", appB.tokenUpdates.first())
        assertFalse(appA === appB)
    }
}
