package zone.ien.firebase.installations.interop

import com.google.firebase.installations.InstallationTokenResult as AndroidTokenResult
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidInstallationTokenResultTest {
    @Test
    fun Android_duration과_creation_epoch_seconds를_expiration_epoch_milliseconds로_노출한다() {
        val native = AndroidTokenResult.builder()
            .setToken("token")
            .setTokenExpirationTimestamp(3_600)
            .setTokenCreationTimestamp(1_710_000_000)
            .build()

        val result = AndroidInstallationTokenResult(native)

        assertEquals(1_710_003_600_000L, result.tokenExpirationTimestamp)
    }
}
