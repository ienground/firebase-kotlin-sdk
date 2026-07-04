package zone.ien.firebase.ai.ondevice

import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.DownloadStatus as AndroidDownloadStatus

@OptIn(PublicPreviewAPI::class)
public actual sealed class DownloadStatus(internal val androidStatus: AndroidDownloadStatus?)

@OptIn(PublicPreviewAPI::class)
public actual class DownloadStarted internal constructor(
    internal val androidStarted: AndroidDownloadStatus.DownloadStarted?,
    private val fallbackBytes: Long = 0L
) : DownloadStatus(androidStarted) {
    public actual val totalBytesToDownload: Long
        get() = androidStarted?.bytesToDownload ?: fallbackBytes

    public actual constructor(totalBytesToDownload: Long) : this(null, totalBytesToDownload)
}

@OptIn(PublicPreviewAPI::class)
public actual class DownloadInProgress internal constructor(
    internal val androidInProgress: AndroidDownloadStatus.DownloadInProgress?,
    private val fallbackBytes: Long = 0L
) : DownloadStatus(androidInProgress) {
    public actual val totalBytesDownloaded: Long
        get() = androidInProgress?.totalBytesDownloaded ?: fallbackBytes

    public actual constructor(totalBytesDownloaded: Long) : this(null, totalBytesDownloaded)
}

@OptIn(PublicPreviewAPI::class)
public actual class DownloadCompleted internal constructor(
    internal val androidCompleted: AndroidDownloadStatus.DownloadCompleted?
) : DownloadStatus(androidCompleted) {
    public actual constructor() : this(null)
}

@OptIn(PublicPreviewAPI::class)
public actual class DownloadFailed internal constructor(
    internal val androidFailed: AndroidDownloadStatus.DownloadFailed?,
    private val fallbackException: Exception? = null
) : DownloadStatus(androidFailed) {
    public actual val exception: Exception
        get() = androidFailed?.exception ?: fallbackException ?: IllegalStateException("Exception is not initialized")

    public actual constructor(exception: Exception) : this(null, exception)
}
