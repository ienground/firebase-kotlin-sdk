package zone.ien.firebase.ai.ondevice

public expect sealed class DownloadStatus

public expect class DownloadStarted : DownloadStatus {
    public val totalBytesToDownload: Long
    public constructor(totalBytesToDownload: Long)
}

public expect class DownloadInProgress : DownloadStatus {
    public val totalBytesDownloaded: Long
    public constructor(totalBytesDownloaded: Long)
}

public expect class DownloadCompleted : DownloadStatus {
    public constructor()
}

public expect class DownloadFailed : DownloadStatus {
    public val exception: Exception
    public constructor(exception: Exception)
}
