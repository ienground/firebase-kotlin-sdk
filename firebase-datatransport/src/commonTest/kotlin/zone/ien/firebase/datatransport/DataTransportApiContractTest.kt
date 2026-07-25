package zone.ien.firebase.datatransport

import kotlin.test.Test
import kotlin.test.assertEquals

class DataTransportApiContractTest {
    @Test
    fun 전송_등록자가_공통_API에_노출된다() {
        assertEquals("TransportRegistrar", TransportRegistrar::class.simpleName)
    }
}
