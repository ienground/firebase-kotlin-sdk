package zone.ien.firebase.ai.ondevice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class OnDeviceStatusTest {
    @Test
    fun 다운로드_시작과_진행_바이트를_보존한다() {
        val started: DownloadStatus = DownloadStarted(1_024)
        val progress: DownloadStatus = DownloadInProgress(512)

        assertEquals(1_024L, assertIs<DownloadStarted>(started).totalBytesToDownload)
        assertEquals(512L, assertIs<DownloadInProgress>(progress).totalBytesDownloaded)
    }

    @Test
    fun 다운로드_완료_상태를_생성한다() {
        assertIs<DownloadCompleted>(DownloadCompleted())
    }

    @Test
    fun 다운로드_실패는_원인_예외를_보존한다() {
        val cause = IllegalStateException("다운로드 실패")
        val failed: DownloadStatus = DownloadFailed(cause)

        assertSame(cause, assertIs<DownloadFailed>(failed).exception)
    }

    @Test
    fun 모델_상태는_지원하는_항목을_모두_제공한다() {
        assertEquals(
            listOf("AVAILABLE", "DOWNLOADABLE", "DOWNLOADING", "UNAVAILABLE"),
            OnDeviceModelStatus.entries.map { it.name }
        )
    }
}
