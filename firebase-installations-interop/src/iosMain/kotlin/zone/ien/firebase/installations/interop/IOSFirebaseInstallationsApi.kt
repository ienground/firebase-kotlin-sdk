package zone.ien.firebase.installations.interop

public class IOSFirebaseInstallationsApi : FirebaseInstallationsApi {

    override suspend fun getId(): String {
        return "dummy-ios-fid"
    }

    override suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult {
        return object : InstallationTokenResult {
            override val token: String = "dummy-ios-token"
            override val tokenExpirationTimestamp: Long = 0L
        }
    }

    override suspend fun delete() {
        // No-op
    }

    override fun clearFidCache() {
        // No-op
    }

    override fun registerFidListener(listener: FidListener): FidListenerHandle {
        return object : FidListenerHandle {
            override fun unregister() {
                // No-op
            }
        }
    }
}
