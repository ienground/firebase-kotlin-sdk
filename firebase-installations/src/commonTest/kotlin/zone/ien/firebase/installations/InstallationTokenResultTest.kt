package zone.ien.firebase.installations

import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.firebase.installations.interop.InstallationTokenResult as InteropTokenResult

class InstallationTokenResultTest {
    @Test
    fun 토큰_정보를_보존하고_상호운용_계약을_구현한다() {
        val result: InteropTokenResult = InstallationTokenResult(
            token = "token",
            tokenExpirationTimestamp = 2_000,
            tokenCreationTimestamp = 1_000
        )

        assertEquals("token", result.token)
        assertEquals(2_000L, result.tokenExpirationTimestamp)
        assertEquals(1_000L, (result as InstallationTokenResult).tokenCreationTimestamp)
    }
}
