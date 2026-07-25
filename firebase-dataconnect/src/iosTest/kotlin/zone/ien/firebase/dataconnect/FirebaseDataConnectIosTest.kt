package zone.ien.firebase.dataconnect

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class FirebaseDataConnectIosTest {
    @Test
    fun 동일한_설정은_동일한_인스턴스를_반환한다() {
        val config = ConnectorConfig("orders", "asia-northeast3", "mobile")

        val first = FirebaseDataConnect.getInstance(config)
        val second = FirebaseDataConnect.getInstance(config.copy())

        assertSame(first, second)
    }

    @Test
    fun 서로_다른_설정은_별도_인스턴스를_반환한다() {
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
    fun 에뮬레이터_주소를_메모리에_보존한다() {
        val dataConnect = FirebaseDataConnect.getInstance(
            ConnectorConfig("payments", "asia-northeast3", "ios-test")
        )

        dataConnect.useEmulator("127.0.0.1", 9399)

        assertEquals("127.0.0.1", dataConnect.emulatorHost)
        assertEquals(9399, dataConnect.emulatorPort)
    }
}
