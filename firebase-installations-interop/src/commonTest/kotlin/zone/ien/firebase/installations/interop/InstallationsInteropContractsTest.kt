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
    fun testRegistrationHandleExposesUnregister() {
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
    fun testTokenResultContractProvidesTokenAndExpiration() {
        val result = object : InstallationTokenResult {
            override val token = "token"
            override val tokenExpirationTimestamp = 3_600L
        }

        assertEquals("token", result.token)
        assertEquals(3_600L, result.tokenExpirationTimestamp)
    }
}
