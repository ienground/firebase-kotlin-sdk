package zone.ien.firebase.messaging

import com.google.firebase.messaging.FirebaseMessagingService as AndroidFirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage as AndroidRemoteMessage
import java.lang.reflect.Modifier
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class FirebaseMessagingIntegrationTest {
    @Test
    fun 기존_2인자_RemoteMessage_JVM_생성자를_보존한다() {
        val constructor = RemoteMessage::class.java.getConstructor(
            Map::class.java,
            Notification::class.java
        )

        assertNotNull(constructor)
    }

    @Test
    fun 제공_서비스는_상속할_수_있다() {
        assertFalse(Modifier.isFinal(FirebaseMessagingService::class.java.modifiers))
    }

    @Test
    fun 기존_서비스는_공개_브리지로_이벤트를_전달할_수_있다() {
        val serviceClass = ExistingFirebaseMessagingService::class.java

        assertNotNull(serviceClass)
    }

    @Test
    fun onNewToken은_default_app_token_상태만_갱신한다() = runBlocking {
        FirebaseMessagingServiceBridge.handleNewToken("default-token")

        assertEquals(
            "default-token",
            MessagingEventDispatcher.tokenEvents(DEFAULT_MESSAGING_APP_IDENTITY).tokenUpdates.first()
        )
    }
}

private class ExistingFirebaseMessagingService : AndroidFirebaseMessagingService() {
    override fun onMessageReceived(message: AndroidRemoteMessage) {
        FirebaseMessagingServiceBridge.handleMessage(message)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        FirebaseMessagingServiceBridge.handleNewToken(token)
    }
}
