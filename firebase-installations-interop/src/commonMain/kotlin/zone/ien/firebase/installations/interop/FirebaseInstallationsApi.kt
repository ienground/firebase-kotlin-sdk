package zone.ien.firebase.installations.interop

public interface FirebaseInstallationsApi {
    public suspend fun getId(): String
    public suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult
    public suspend fun delete()
    public fun clearFidCache()
    public fun registerFidListener(listener: FidListener): FidListenerHandle
}
