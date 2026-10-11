package zone.ien.firebase.sessions

import kotlin.test.Test
import kotlin.test.assertEquals

class SessionsApiContractTest {
    @Test
    fun testSessionsTypesExposedInCommonApi() {
        assertEquals("FirebaseSessions", FirebaseSessions::class.simpleName)
    }
}
