package zone.ien.firebase.messaging.directboot

import kotlin.test.Test
import kotlin.test.assertEquals

class DirectBootApiContractTest {
    @Test
    fun testDirectBootTypesExposedInCommonApi() {
        assertEquals("FirebaseMessagingDirectBoot", FirebaseMessagingDirectBoot::class.simpleName)
    }
}
