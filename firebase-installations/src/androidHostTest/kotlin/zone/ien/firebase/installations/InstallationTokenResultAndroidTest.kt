package zone.ien.firebase.installations

import com.google.firebase.installations.InstallationTokenResult as AndroidInstallationTokenResult
import kotlin.test.Test
import kotlin.test.assertEquals

class InstallationTokenResultAndroidTest {
    @Test
    fun Android_duration과_epoch_seconds를_epoch_milliseconds로_변환한다() {
        val androidResult = AndroidInstallationTokenResult.builder()
            .setToken("token")
            .setTokenExpirationTimestamp(3_600)
            .setTokenCreationTimestamp(1_710_000_000)
            .build()

        val result = androidResult.toCommonInstallationTokenResult()

        assertEquals(1_710_003_600_000L, result.tokenExpirationTimestamp)
        assertEquals(1_710_000_000_000L, result.tokenCreationTimestamp)
    }
}
