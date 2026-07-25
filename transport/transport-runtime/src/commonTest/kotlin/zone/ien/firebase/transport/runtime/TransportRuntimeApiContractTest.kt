package zone.ien.firebase.transport.runtime

import kotlin.test.Test
import kotlin.test.assertEquals

class TransportRuntimeApiContractTest {
    @Test
    fun 런타임_계약이_공통_API에_노출된다() {
        assertEquals("TransportRuntime", TransportRuntime::class.simpleName)
        assertEquals("Destination", Destination::class.simpleName)
    }
}
