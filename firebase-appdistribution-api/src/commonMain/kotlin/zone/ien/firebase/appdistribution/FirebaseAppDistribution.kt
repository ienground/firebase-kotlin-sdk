package zone.ien.firebase.appdistribution

import kotlinx.coroutines.flow.Flow

public interface FirebaseAppDistributionApi {
    public val isTesterSignedIn: Boolean
    public suspend fun signInTester()
    public fun signOutTester()
    public suspend fun checkForNewRelease(): AppDistributionRelease?
    public fun updateIfNewReleaseAvailable(): Flow<UpdateProgress>
}
