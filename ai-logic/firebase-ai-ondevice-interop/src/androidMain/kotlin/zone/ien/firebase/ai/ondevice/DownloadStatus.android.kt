package zone.ien.firebase.ai.ondevice

import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.DownloadStatus as AndroidDownloadStatus

@OptIn(PublicPreviewAPI::class)
public actual sealed class DownloadStatus(internal val androidStatus: AndroidDownloadStatus?)

@OptIn(PublicPreviewAPI::class)
public actual class DownloadStarted(internal val androidStarted: AndroidDownloadStatus.DownloadStarted?) : DownloadStatus(androidStarted) {
    public actual val totalBytesToDownload: Long
        get() = androidStarted?.bytesToDownload ?: _totalBytesToDownload

    private var _totalBytesToDownload: Long = 0

    public actual constructor(
        totalBytesToDownload: Long
    ) : this(null) {
        this._totalBytesToDownload = totalBytesToDownload
    }
}

@OptIn(PublicPreviewAPI::class)
public actual class DownloadInProgress(internal val androidInProgress: AndroidDownloadStatus.DownloadInProgress?) : DownloadStatus(androidInProgress) {
    public actual val totalBytesDownloaded: Long
        get() = androidInProgress?.totalBytesDownloaded ?: _totalBytesDownloaded

    private var _totalBytesDownloaded: Long = 0

    public actual constructor(
        totalBytesDownloaded: Long
    ) : this(null) {
        this._totalBytesDownloaded = totalBytesDownloaded
    }
}

@OptIn(PublicPreviewAPI::class)
public actual class DownloadCompleted(internal val androidCompleted: AndroidDownloadStatus.DownloadCompleted?) : DownloadStatus(androidCompleted) {
    public actual constructor() : this(null)
}

@OptIn(PublicPreviewAPI::class)
public actual class DownloadFailed(internal val androidFailed: AndroidDownloadStatus.DownloadFailed?) : DownloadStatus(androidFailed) {
    public actual val exception: Exception
        get() = androidFailed?.exception ?: _exception!!

    private var _exception: Exception? = null

    public actual constructor(exception: Exception) : this(null) {
        this._exception = exception
    }
}
