package zone.ien.firebase.installations.interop

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InstallationsInteropContractsTest {
    @Test
    fun FID_리스너는_변경된_식별자를_전달한다() {
        var received = ""
        val listener = FidListener { received = it }

        listener.onFidChanged("fid-123")

        assertEquals("fid-123", received)
    }

    @Test
    fun 등록_핸들은_해제_동작을_노출한다() {
        var unregistered = false
        val handle = object : FidListenerHandle {
            override fun unregister() {
                unregistered = true
            }
        }

        handle.unregister()

        assertTrue(unregistered)
    }

    @Test
    fun 토큰_결과_계약은_토큰과_만료시각을_제공한다() {
        val result = object : InstallationTokenResult {
            override val token = "token"
            override val tokenExpirationTimestamp = 3_600L
        }

        assertEquals("token", result.token)
        assertEquals(3_600L, result.tokenExpirationTimestamp)
    }
}
