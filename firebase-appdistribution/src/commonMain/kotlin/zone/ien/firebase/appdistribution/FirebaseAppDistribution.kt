package zone.ien.firebase.appdistribution

import kotlinx.coroutines.flow.Flow

public expect class FirebaseAppDistribution : FirebaseAppDistributionApi {
    public override val isTesterSignedIn: Boolean
    public override suspend fun signInTester()
    public override fun signOutTester()
    public override suspend fun checkForNewRelease(): AppDistributionRelease?
    public override fun updateIfNewReleaseAvailable(): Flow<UpdateProgress>

    public companion object {
        public val instance: FirebaseAppDistribution
    }
}
