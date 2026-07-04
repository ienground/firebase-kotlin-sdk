package zone.ien.firebase.appdistribution

import com.google.android.gms.tasks.Task
import com.google.firebase.appdistribution.OnProgressListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import zone.ien.firebase.FirebaseApp
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal fun com.google.firebase.appdistribution.FirebaseAppDistributionException.toKmp(): FirebaseAppDistributionException {
    val kmpStatus = when (this.errorCode) {
        com.google.firebase.appdistribution.FirebaseAppDistributionException.Status.AUTHENTICATION_FAILURE -> FirebaseAppDistributionException.Status.AUTHENTICATION_FAILURE
        com.google.firebase.appdistribution.FirebaseAppDistributionException.Status.AUTHENTICATION_CANCELED -> FirebaseAppDistributionException.Status.AUTHENTICATION_CANCELED
        com.google.firebase.appdistribution.FirebaseAppDistributionException.Status.NETWORK_FAILURE -> FirebaseAppDistributionException.Status.NETWORK_FAILURE
        com.google.firebase.appdistribution.FirebaseAppDistributionException.Status.UPDATE_NOT_AVAILABLE -> FirebaseAppDistributionException.Status.UPDATE_NOT_AVAILABLE
        com.google.firebase.appdistribution.FirebaseAppDistributionException.Status.INSTALLATION_FAILURE -> FirebaseAppDistributionException.Status.INSTALLATION_FAILURE
        else -> FirebaseAppDistributionException.Status.UNKNOWN
    }
    return FirebaseAppDistributionException(message, kmpStatus)
}

internal suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result)
        } else {
            val exception = task.exception ?: Exception("Task failed with no exception details.")
            if (exception is com.google.firebase.appdistribution.FirebaseAppDistributionException) {
                continuation.resumeWithException(exception.toKmp())
            } else {
                continuation.resumeWithException(exception)
            }
        }
    }
}

public actual class FirebaseAppDistribution internal constructor(
    private val androidAppDistribution: com.google.firebase.appdistribution.FirebaseAppDistribution
) {
    public actual val isTesterSignedIn: Boolean
        get() = androidAppDistribution.isTesterSignedIn

    public actual suspend fun signInTester() {
        androidAppDistribution.signInTester().await()
    }

    public actual fun signOutTester() {
        androidAppDistribution.signOutTester()
    }

    public actual suspend fun checkForNewRelease(): AppDistributionRelease? {
        val nativeRelease = androidAppDistribution.checkForNewRelease().await() ?: return null
        return AppDistributionRelease(
            displayVersion = nativeRelease.displayVersion,
            versionCode = nativeRelease.versionCode,
            releaseNotes = nativeRelease.releaseNotes,
            binaryType = nativeRelease.binaryType.name
        )
    }

    public actual fun updateIfNewReleaseAvailable(): Flow<UpdateProgress> = callbackFlow {
        val updateTask = androidAppDistribution.updateIfNewReleaseAvailable()
        val listener = OnProgressListener { progress ->
            val kmpStatus = when (progress.updateStatus) {
                com.google.firebase.appdistribution.UpdateStatus.PENDING -> UpdateStatus.PENDING
                com.google.firebase.appdistribution.UpdateStatus.DOWNLOADING -> UpdateStatus.DOWNLOADING
                com.google.firebase.appdistribution.UpdateStatus.DOWNLOADED -> UpdateStatus.DOWNLOADED
                com.google.firebase.appdistribution.UpdateStatus.DOWNLOAD_FAILED -> UpdateStatus.DOWNLOAD_FAILED
                com.google.firebase.appdistribution.UpdateStatus.INSTALL_CANCELED -> UpdateStatus.INSTALL_CANCELED
                com.google.firebase.appdistribution.UpdateStatus.INSTALL_FAILED -> UpdateStatus.INSTALL_FAILED
                com.google.firebase.appdistribution.UpdateStatus.UPDATE_CANCELED -> UpdateStatus.UPDATE_CANCELED
                else -> UpdateStatus.PENDING
            }
            trySend(
                UpdateProgress(
                    apkBytesDownloaded = progress.apkBytesDownloaded,
                    apkFileTotalBytes = progress.apkFileTotalBytes,
                    updateStatus = kmpStatus
                )
            )
        }
        updateTask.addOnProgressListener(listener)
        updateTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                channel.close()
            } else {
                val exception = task.exception ?: Exception("Update task finished with no exception.")
                if (exception is com.google.firebase.appdistribution.FirebaseAppDistributionException) {
                    channel.close(exception.toKmp())
                } else {
                    channel.close(exception)
                }
            }
        }
        awaitClose {
            // OnProgressListener removal is not supported in the Android SDK API
        }
    }

    public actual companion object {
        public actual val instance: FirebaseAppDistribution
            get() = FirebaseAppDistribution(com.google.firebase.appdistribution.FirebaseAppDistribution.getInstance())
    }
}
