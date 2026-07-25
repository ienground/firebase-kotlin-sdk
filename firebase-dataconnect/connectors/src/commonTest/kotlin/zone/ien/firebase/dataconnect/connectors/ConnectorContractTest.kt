package zone.ien.firebase.dataconnect.connectors

import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.firebase.dataconnect.ConnectorConfig

class ConnectorContractTest {
    @Test
    fun 생성형_커넥터가_사용할_설정값을_구분한다() {
        val production = ConnectorConfig("orders", "asia-northeast3", "mobile")
        val emulator = production.copy(location = "local")

        assertEquals("orders", production.service)
        assertEquals("asia-northeast3", production.location)
        assertEquals("local", emulator.location)
    }
}
