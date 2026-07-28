package zone.ien.firebase.dataconnect

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class FirebaseDataConnectIosTest {
    @Test
    fun testSameConfigReturnsSameInstance() {
        val config = ConnectorConfig("orders", "asia-northeast3", "mobile")

        val first = FirebaseDataConnect.getInstance(config)
        val second = FirebaseDataConnect.getInstance(config.copy())

        assertSame(first, second)
    }

    @Test
    fun testDifferentConfigsReturnSeparateInstances() {
        val first = FirebaseDataConnect.getInstance(
            ConnectorConfig("orders", "asia-northeast3", "mobile")
        )
        val second = FirebaseDataConnect.getInstance(
            ConnectorConfig("orders", "us-central1", "mobile")
        )

        assertEquals("asia-northeast3", first.config.location)
        assertEquals("us-central1", second.config.location)
    }

    @Test
    fun testPreservesEmulatorAddressInMemory() {
        val dataConnect = FirebaseDataConnect.getInstance(
            ConnectorConfig("payments", "asia-northeast3", "ios-test")
        )

        dataConnect.useEmulator("127.0.0.1", 9399)

        assertEquals("127.0.0.1", dataConnect.emulatorHost)
        assertEquals(9399, dataConnect.emulatorPort)
    }
}
