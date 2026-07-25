package zone.ien.firebase.inappmessaging

import kotlin.test.Test
import kotlin.test.assertEquals

class InAppMessagingApiContractTest {
    @Test
    fun 인앱_메시징_타입이_공통_API에_노출된다() {
        assertEquals("FirebaseInAppMessaging", FirebaseInAppMessaging::class.simpleName)
    }
}
