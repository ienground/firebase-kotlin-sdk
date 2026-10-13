package zone.ien.firebase.installations.interop

import com.google.firebase.installations.FirebaseInstallationsApi as AndroidInstallationsApi
import kotlinx.coroutines.tasks.await

public class AndroidFirebaseInstallationsApi(
    public val androidInstallationsApi: AndroidInstallationsApi
) : FirebaseInstallationsApi {

    override suspend fun getId(): String {
        return androidInstallationsApi.id.await()
    }

    override suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult {
        val result = androidInstallationsApi.getToken(forceRefresh).await()
        return AndroidInstallationTokenResult(result)
    }

    override suspend fun delete() {
        androidInstallationsApi.delete().await()
    }

    override fun clearFidCache() {
        androidInstallationsApi.clearFidCache()
    }

    override fun registerFidListener(listener: FidListener): FidListenerHandle {
        val androidListener = AndroidFidListener(listener)
        val handle = androidInstallationsApi.registerFidListener(androidListener)
        return AndroidFidListenerHandle(handle)
    }
}
