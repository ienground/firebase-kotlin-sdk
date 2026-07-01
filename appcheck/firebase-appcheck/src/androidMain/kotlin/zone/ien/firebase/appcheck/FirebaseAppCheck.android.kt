package zone.ien.firebase.appcheck

import zone.ien.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck as AndroidFirebaseAppCheck

public actual class FirebaseAppCheck private actual constructor() {
    private lateinit var androidAppCheck: AndroidFirebaseAppCheck

    internal constructor(androidAppCheck: AndroidFirebaseAppCheck) : this() {
        this.androidAppCheck = androidAppCheck
    }

    public actual fun installAppCheckProviderFactory(factory: AppCheckProviderFactory) {
        androidAppCheck.installAppCheckProviderFactory(factory.androidFactory)
    }

    public actual fun setTokenAutoRefreshEnabled(enabled: Boolean) {
        androidAppCheck.setTokenAutoRefreshEnabled(enabled)
    }

    public actual suspend fun getToken(forceRefresh: Boolean): AppCheckToken {
        return AppCheckToken(androidAppCheck.getToken(forceRefresh).await())
    }

    public actual suspend fun getLimitedUseToken(): AppCheckToken {
        return AppCheckToken(androidAppCheck.limitedUseToken.await())
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseAppCheck {
            return FirebaseAppCheck(AndroidFirebaseAppCheck.getInstance())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseAppCheck {
            return FirebaseAppCheck(AndroidFirebaseAppCheck.getInstance(app.androidApp))
        }
    }
}
