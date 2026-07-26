package zone.ien.firebase.appcheck

import zone.ien.firebase.Firebase
import zone.ien.firebase.FirebaseApp

public expect class FirebaseAppCheck {
    private constructor()
    public fun installAppCheckProviderFactory(factory: AppCheckProviderFactory)
    public fun setTokenAutoRefreshEnabled(enabled: Boolean)
    public suspend fun getToken(forceRefresh: Boolean): AppCheckToken
    public suspend fun getLimitedUseToken(): AppCheckToken

    public companion object {
        public fun getInstance(): FirebaseAppCheck
        public fun getInstance(app: FirebaseApp): FirebaseAppCheck
    }
}

public val Firebase.appCheck: FirebaseAppCheck
    get() = FirebaseAppCheck.getInstance()

public fun Firebase.appCheck(app: FirebaseApp): FirebaseAppCheck =
    FirebaseAppCheck.getInstance(app)
