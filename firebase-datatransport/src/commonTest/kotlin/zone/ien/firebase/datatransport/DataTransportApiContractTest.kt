package zone.ien.firebase.datatransport

import kotlin.test.Test
import kotlin.test.assertEquals

class DataTransportApiContractTest {
    @Test
    fun testTransportRegistrarExposedInCommonApi() {
        assertEquals("TransportRegistrar", TransportRegistrar::class.simpleName)
    }
}
