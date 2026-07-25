package zone.ien.firebase.sessions

import kotlin.test.Test
import kotlin.test.assertEquals

class SessionsApiContractTest {
    @Test
    fun 세션_타입이_공통_API에_노출된다() {
        assertEquals("FirebaseSessions", FirebaseSessions::class.simpleName)
    }
}
