package zone.ien.firebase.dataconnect

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ConnectorConfigTest {
    @Test
    fun testConnectorConfigEqualityAndCopy() {
        val config = ConnectorConfig("orders", "asia-northeast3", "mobile")
        val same = ConnectorConfig("orders", "asia-northeast3", "mobile")
        val copied = config.copy(connector = "admin")

        assertEquals(config, same)
        assertEquals(config.hashCode(), same.hashCode())
        assertNotEquals(config, copied)
        assertEquals("admin", copied.connector)
    }
}
