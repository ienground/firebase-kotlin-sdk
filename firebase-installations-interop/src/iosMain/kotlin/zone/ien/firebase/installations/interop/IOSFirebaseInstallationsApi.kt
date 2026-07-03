package zone.ien.firebase.installations.interop

public class IOSFirebaseInstallationsApi : FirebaseInstallationsApi {
    override suspend fun getId(): String {
        throw UnsupportedOperationException("Installations Interop is not supported on iOS.")
    }

    override suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult {
        throw UnsupportedOperationException("Installations Interop is not supported on iOS.")
    }

    override suspend fun delete() {
        throw UnsupportedOperationException("Installations Interop is not supported on iOS.")
    }

    override fun clearFidCache() {
        throw UnsupportedOperationException("Installations Interop is not supported on iOS.")
    }

    override fun registerFidListener(listener: FidListener): FidListenerHandle {
        throw UnsupportedOperationException("Installations Interop is not supported on iOS.")
    }
}
