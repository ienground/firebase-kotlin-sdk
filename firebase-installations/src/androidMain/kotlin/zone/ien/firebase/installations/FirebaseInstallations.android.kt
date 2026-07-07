package zone.ien.firebase.installations

import com.google.firebase.installations.FirebaseInstallations as AndroidFirebaseInstallations
import kotlinx.coroutines.tasks.await
import zone.ien.firebase.FirebaseApp

import zone.ien.firebase.installations.interop.FirebaseInstallationsApi
import zone.ien.firebase.installations.interop.FidListener
import zone.ien.firebase.installations.interop.FidListenerHandle
import zone.ien.firebase.installations.interop.AndroidFidListener
import zone.ien.firebase.installations.interop.AndroidFidListenerHandle

public actual class FirebaseInstallations(
    private val androidInstallations: AndroidFirebaseInstallations
) : FirebaseInstallationsApi {
    actual override suspend fun getId(): String {
        return androidInstallations.id.await()
    }

    actual override suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult {
        val result = androidInstallations.getToken(forceRefresh).await()
        return InstallationTokenResult(
            token = result.token,
            tokenExpirationTimestamp = result.tokenExpirationTimestamp,
            tokenCreationTimestamp = result.tokenCreationTimestamp
        )
    }

    actual override suspend fun delete() {
        androidInstallations.delete().await()
    }

    actual override fun clearFidCache() {
        androidInstallations.clearFidCache()
    }

    actual override fun registerFidListener(listener: FidListener): FidListenerHandle {
        val androidListener = AndroidFidListener(listener)
        val handle = androidInstallations.registerFidListener(androidListener)
        return AndroidFidListenerHandle(handle)
    }

    public actual companion object {
        public actual val instance: FirebaseInstallations
            get() = FirebaseInstallations(AndroidFirebaseInstallations.getInstance())

        public actual fun getInstance(app: FirebaseApp): FirebaseInstallations {
            return FirebaseInstallations(AndroidFirebaseInstallations.getInstance(app.androidApp))
        }
    }
}
