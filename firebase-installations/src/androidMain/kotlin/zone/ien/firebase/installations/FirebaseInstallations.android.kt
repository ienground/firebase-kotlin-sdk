package zone.ien.firebase.installations

import com.google.firebase.installations.FirebaseInstallations as AndroidFirebaseInstallations
import kotlinx.coroutines.tasks.await
import zone.ien.firebase.FirebaseApp

public actual class FirebaseInstallations(
    private val androidInstallations: AndroidFirebaseInstallations
) {
    public actual suspend fun getId(): String {
        return androidInstallations.id.await()
    }

    public actual suspend fun getToken(forceRefresh: Boolean): InstallationTokenResult {
        val result = androidInstallations.getToken(forceRefresh).await()
        return InstallationTokenResult(
            token = result.token,
            tokenExpirationTimestamp = result.tokenExpirationTimestamp,
            tokenCreationTimestamp = result.tokenCreationTimestamp
        )
    }

    public actual suspend fun delete() {
        androidInstallations.delete().await()
    }

    public actual companion object {
        public actual val instance: FirebaseInstallations
            get() = FirebaseInstallations(AndroidFirebaseInstallations.getInstance())

        public actual fun getInstance(app: FirebaseApp): FirebaseInstallations {
            return FirebaseInstallations(AndroidFirebaseInstallations.getInstance(app.androidApp))
        }
    }
}
