package zone.ien.firebase.installations

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.timeIntervalSince1970
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.installations.FIRInstallations
import swiftPMImport.zone.ien.firebase.firebase.installations.FIRInstallationsAuthTokenResult
import zone.ien.firebase.FirebaseApp

import zone.ien.firebase.installations.interop.FirebaseInstallationsApi
import zone.ien.firebase.installations.interop.FidListener
import zone.ien.firebase.installations.interop.FidListenerHandle

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseInstallations(
    private val iosInstallations: FIRInstallations
) : FirebaseInstallationsApi {
    private val fidMemoryState: FidMemoryState = FidMemoryState()

    actual override suspend fun getId(): String = suspendCancellableCoroutine { continuation ->
        iosInstallations.installationIDWithCompletion { id, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else if (id != null) {
                fidMemoryState.recordFid(id)
                continuation.resume(id)
            } else {
                continuation.resumeWithException(Exception("Installation ID fetch returned null values."))
            }
        }
    }

    actual override suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult = suspendCancellableCoroutine { continuation ->
        iosInstallations.authTokenForcingRefresh(forceRefresh) { result, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else if (result != null) {
                val token = result.authToken
                val expirationDate = result.expirationDate
                val expirationTimestamp = (expirationDate.timeIntervalSince1970 * 1000).toLong()
                continuation.resume(
                    InstallationTokenResult(
                        token = token,
                        tokenExpirationTimestamp = expirationTimestamp,
                        tokenCreationTimestamp = InstallationTokenResult.UNAVAILABLE_TOKEN_CREATION_TIMESTAMP
                    )
                )
            } else {
                continuation.resumeWithException(Exception("Installation Auth Token fetch returned null values."))
            }
        }
    }

    actual override suspend fun delete(): Unit = suspendCancellableCoroutine { continuation ->
        iosInstallations.deleteWithCompletion { error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else {
                continuation.resume(Unit)
            }
        }
    }

    actual override fun clearFidCache() {
        fidMemoryState.clearFidCache()
    }

    actual override fun registerFidListener(listener: FidListener): FidListenerHandle {
        return fidMemoryState.registerFidListener(listener)
    }

    public actual companion object {
        private val instances: IdentityInstanceCache<FirebaseInstallations> = IdentityInstanceCache()

        public actual val instance: FirebaseInstallations
            get() = instances.getOrCreate(FirebaseApp.instance.getName()) {
                FirebaseInstallations(FIRInstallations.installations())
            }

        public actual fun getInstance(app: FirebaseApp): FirebaseInstallations {
            return instances.getOrCreate(app.getName()) {
                FirebaseInstallations(FIRInstallations.installationsWithApp(app.iosApp))
            }
        }
    }
}
