package zone.ien.firebase.installations

import zone.ien.firebase.FirebaseApp

import zone.ien.firebase.installations.interop.FirebaseInstallationsApi
import zone.ien.firebase.installations.interop.FidListener
import zone.ien.firebase.installations.interop.FidListenerHandle

public expect class FirebaseInstallations : FirebaseInstallationsApi {
    override public suspend fun getId(): String
    override public suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult
    override public suspend fun delete()
    override public fun clearFidCache()
    override public fun registerFidListener(listener: FidListener): FidListenerHandle

    public companion object {
        public val instance: FirebaseInstallations
        public fun getInstance(app: FirebaseApp): FirebaseInstallations
    }
}
