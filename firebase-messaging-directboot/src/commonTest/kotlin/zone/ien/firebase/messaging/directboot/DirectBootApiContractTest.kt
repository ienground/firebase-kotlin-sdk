package zone.ien.firebase.messaging.directboot

import kotlin.test.Test
import kotlin.test.assertEquals

class DirectBootApiContractTest {
    @Test
    fun 다이렉트_부트_타입이_공통_API에_노출된다() {
        assertEquals("FirebaseMessagingDirectBoot", FirebaseMessagingDirectBoot::class.simpleName)
    }
}
