package zone.ien.firebase.ai.ondevice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class OnDeviceStatusTest {
    @Test
    fun testPreservesDownloadStartAndProgressBytes() {
        val started: DownloadStatus = DownloadStarted(1_024)
        val progress: DownloadStatus = DownloadInProgress(512)

        assertEquals(1_024L, assertIs<DownloadStarted>(started).totalBytesToDownload)
        assertEquals(512L, assertIs<DownloadInProgress>(progress).totalBytesDownloaded)
    }

    @Test
    fun testCreatesDownloadCompletedStatus() {
        assertIs<DownloadCompleted>(DownloadCompleted())
    }

    @Test
    fun testDownloadFailedPreservesCauseException() {
        val cause = IllegalStateException("다운로드 실패")
        val failed: DownloadStatus = DownloadFailed(cause)

        assertSame(cause, assertIs<DownloadFailed>(failed).exception)
    }

    @Test
    fun testModelStatusProvidesAllSupportedValues() {
        assertEquals(
            listOf("AVAILABLE", "DOWNLOADABLE", "DOWNLOADING", "UNAVAILABLE"),
            OnDeviceModelStatus.entries.map { it.name }
        )
    }
}
