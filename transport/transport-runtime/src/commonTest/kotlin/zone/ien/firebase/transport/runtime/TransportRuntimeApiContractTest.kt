package zone.ien.firebase.transport.runtime

import kotlin.test.Test
import kotlin.test.assertEquals

class TransportRuntimeApiContractTest {
    @Test
    fun testRuntimeContractExposedInCommonApi() {
        assertEquals("TransportRuntime", TransportRuntime::class.simpleName)
        assertEquals("Destination", Destination::class.simpleName)
    }
}
