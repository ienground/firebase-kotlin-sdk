package zone.ien.firebase.dataconnect

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ConnectorConfigTest {
    @Test
    fun 커넥터_설정을_값으로_비교하고_복사한다() {
        val config = ConnectorConfig("orders", "asia-northeast3", "mobile")
        val same = ConnectorConfig("orders", "asia-northeast3", "mobile")
        val copied = config.copy(connector = "admin")

        assertEquals(config, same)
        assertEquals(config.hashCode(), same.hashCode())
        assertNotEquals(config, copied)
        assertEquals("admin", copied.connector)
    }
}
