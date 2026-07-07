package zone.ien.firebase.installations

import zone.ien.firebase.FirebaseApp

import zone.ien.firebase.installations.interop.FirebaseInstallationsApi
import zone.ien.firebase.installations.interop.FidListener
import zone.ien.firebase.installations.interop.FidListenerHandle

public expect class FirebaseInstallations : FirebaseInstallationsApi {
    public override suspend fun getId(): String
    public override suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult
    public override suspend fun delete()
    public override fun clearFidCache()
    public override fun registerFidListener(listener: FidListener): FidListenerHandle

    public companion object {
        public val instance: FirebaseInstallations
        public fun getInstance(app: FirebaseApp): FirebaseInstallations
    }
}
