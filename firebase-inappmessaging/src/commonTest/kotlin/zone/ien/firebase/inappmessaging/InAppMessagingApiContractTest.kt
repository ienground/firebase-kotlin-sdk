package zone.ien.firebase.inappmessaging

import kotlin.test.Test
import kotlin.test.assertEquals

class InAppMessagingApiContractTest {
    @Test
    fun testInAppMessagingTypesExposedInCommonApi() {
        assertEquals("FirebaseInAppMessaging", FirebaseInAppMessaging::class.simpleName)
    }
}
