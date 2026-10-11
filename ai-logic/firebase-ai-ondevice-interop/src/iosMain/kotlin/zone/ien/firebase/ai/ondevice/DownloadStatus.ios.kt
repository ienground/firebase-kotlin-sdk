package zone.ien.firebase.ai.ondevice

public actual sealed class DownloadStatus

public actual class DownloadStarted actual constructor(
    public actual val totalBytesToDownload: Long
) : DownloadStatus()

public actual class DownloadInProgress actual constructor(
    public actual val totalBytesDownloaded: Long
) : DownloadStatus()

public actual class DownloadCompleted actual constructor() : DownloadStatus()

public actual class DownloadFailed actual constructor(
    public actual val exception: Exception
) : DownloadStatus()
