package zone.ien.firebase.messaging

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MessagingModelsTest {
    @Test
    fun 알림과_데이터_메시지를_구성한다() {
        val notification = Notification("제목", "본문")
        val message = RemoteMessage(mapOf("route" to "inbox"), notification)

        assertEquals("제목", message.notification?.title)
        assertEquals("본문", message.notification?.body)
        assertEquals("inbox", message.data["route"])
    }

    @Test
    fun 알림이_없는_데이터_메시지를_지원한다() {
        val message = RemoteMessage(emptyMap(), null)

        assertNull(message.notification)
        assertEquals(emptyMap(), message.data)
    }
}
