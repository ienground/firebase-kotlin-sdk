package zone.ien.firebase.appdistribution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppDistributionModelsTest {
    @Test
    fun 릴리스_정보를_보존한다() {
        val release = AppDistributionRelease("2.1.0", 21, null, "APK")

        assertEquals("2.1.0", release.displayVersion)
        assertEquals(21L, release.versionCode)
        assertNull(release.releaseNotes)
        assertEquals("APK", release.binaryType)
    }

    @Test
    fun 업데이트_진행률을_보존한다() {
        val progress = UpdateProgress(512, 1_024, UpdateStatus.DOWNLOADING)

        assertEquals(512L, progress.apkBytesDownloaded)
        assertEquals(1_024L, progress.apkFileTotalBytes)
        assertEquals(UpdateStatus.DOWNLOADING, progress.updateStatus)
    }

    @Test
    fun 예외는_메시지와_상태를_보존한다() {
        val exception = FirebaseAppDistributionException(
            "인증 실패",
            FirebaseAppDistributionException.Status.AUTHENTICATION_FAILURE
        )

        assertEquals("인증 실패", exception.message)
        assertEquals(
            FirebaseAppDistributionException.Status.AUTHENTICATION_FAILURE,
            exception.status
        )
    }
}
