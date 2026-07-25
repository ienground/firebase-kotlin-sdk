package zone.ien.firebase.transport.cct

import kotlin.test.Test
import kotlin.test.assertEquals

class CctBackendApiContractTest {
    @Test
    fun CCT_백엔드_타입이_공통_API에_노출된다() {
        assertEquals("CCTDestination", CCTDestination::class.simpleName)
    }
}
