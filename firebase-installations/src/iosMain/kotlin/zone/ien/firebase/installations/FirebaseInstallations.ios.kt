package zone.ien.firebase.installations

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.timeIntervalSince1970
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.installations.FIRInstallations
import swiftPMImport.zone.ien.firebase.firebase.installations.FIRInstallationsAuthTokenResult
import zone.ien.firebase.FirebaseApp

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseInstallations(
    private val iosInstallations: FIRInstallations
) {
    public actual suspend fun getId(): String = suspendCancellableCoroutine { continuation ->
        iosInstallations.installationIDWithCompletion { id, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else if (id != null) {
                continuation.resume(id)
            } else {
                continuation.resumeWithException(Exception("Installation ID fetch returned null values."))
            }
        }
    }

    public actual suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult = suspendCancellableCoroutine { continuation ->
        iosInstallations.authTokenForcingRefresh(forceRefresh) { result, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else if (result != null) {
                val token = result.authToken
                val expirationDate = result.expirationDate
                val expirationTimestamp = (expirationDate.timeIntervalSince1970 * 1000).toLong()
                val creationTimestamp = (platform.Foundation.NSDate().timeIntervalSince1970 * 1000).toLong()
                continuation.resume(
                    InstallationTokenResult(
                        token = token,
                        tokenExpirationTimestamp = expirationTimestamp,
                        tokenCreationTimestamp = creationTimestamp
                    )
                )
            } else {
                continuation.resumeWithException(Exception("Installation Auth Token fetch returned null values."))
            }
        }
    }

    public actual suspend fun delete(): Unit = suspendCancellableCoroutine { continuation ->
        iosInstallations.deleteWithCompletion { error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else {
                continuation.resume(Unit)
            }
        }
    }

    public actual companion object {
        public actual val instance: FirebaseInstallations
            get() = FirebaseInstallations(FIRInstallations.installations())

        public actual fun getInstance(app: FirebaseApp): FirebaseInstallations {
            return FirebaseInstallations(FIRInstallations.installationsWithApp(app.iosApp))
        }
    }
}
