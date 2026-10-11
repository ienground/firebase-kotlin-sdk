package zone.ien.firebase.appdistribution

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import swiftPMImport.zone.ien.firebase.firebase.appdistribution.FIRAppDistribution
import swiftPMImport.zone.ien.firebase.firebase.appdistribution.FIRAppDistributionRelease
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseAppDistribution private constructor(
    private val iosAppDistribution: FIRAppDistribution
) : FirebaseAppDistributionApi {
    public actual override val isTesterSignedIn: Boolean
        get() = iosAppDistribution.isTesterSignedIn()

    public actual override suspend fun signInTester(): Unit = suspendCancellableCoroutine { continuation ->
        iosAppDistribution.signInTesterWithCompletion { error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else {
                continuation.resume(Unit)
            }
        }
    }

    public actual override fun signOutTester() {
        iosAppDistribution.signOutTester()
    }

    public actual override suspend fun checkForNewRelease(): AppDistributionRelease? = suspendCancellableCoroutine { continuation ->
        iosAppDistribution.checkForUpdateWithCompletion { release, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else if (release != null) {
                val parsedCode = release.buildVersion.toLongOrNull() ?: 0L
                continuation.resume(
                    AppDistributionRelease(
                        displayVersion = release.displayVersion,
                        versionCode = parsedCode,
                        releaseNotes = release.releaseNotes,
                        binaryType = "IPA"
                    )
                )
            } else {
                continuation.resume(null)
            }
        }
    }

    public actual override fun updateIfNewReleaseAvailable(): Flow<UpdateProgress> {
        throw UnsupportedOperationException("In-app update progress monitoring is not supported on iOS.")
    }

    public actual companion object {
        public actual val instance: FirebaseAppDistribution
            get() = FirebaseAppDistribution(FIRAppDistribution.appDistribution())
    }
}
