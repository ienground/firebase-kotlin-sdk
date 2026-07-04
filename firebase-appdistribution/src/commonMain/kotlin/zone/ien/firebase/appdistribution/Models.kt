package zone.ien.firebase.appdistribution

public class AppDistributionRelease(
    public val displayVersion: String,
    public val versionCode: Long,
    public val releaseNotes: String?,
    public val binaryType: String
)

public enum class UpdateStatus {
    PENDING,
    DOWNLOADING,
    DOWNLOADED,
    DOWNLOAD_FAILED,
    INSTALL_CANCELED,
    INSTALL_FAILED,
    UPDATE_CANCELED
}

public class UpdateProgress(
    public val apkBytesDownloaded: Long,
    public val apkFileTotalBytes: Long,
    public val updateStatus: UpdateStatus
)

public class FirebaseAppDistributionException(
    override val message: String?,
    public val status: Status
) : Exception(message) {
    public enum class Status {
        UNKNOWN,
        AUTHENTICATION_FAILURE,
        AUTHENTICATION_CANCELED,
        NETWORK_FAILURE,
        UPDATE_NOT_AVAILABLE,
        INSTALLATION_FAILURE
    }
}
