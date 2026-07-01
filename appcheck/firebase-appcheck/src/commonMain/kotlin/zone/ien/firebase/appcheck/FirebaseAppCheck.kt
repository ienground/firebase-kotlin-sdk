package zone.ien.firebase.appcheck

import zone.ien.firebase.FirebaseApp

public expect class FirebaseAppCheck {
    public fun installAppCheckProviderFactory(factory: AppCheckProviderFactory)
    public fun setTokenAutoRefreshEnabled(enabled: Boolean)
    public suspend fun getToken(forceRefresh: Boolean): AppCheckToken
    public suspend fun getLimitedUseToken(): AppCheckToken

    public companion object {
        public fun getInstance(): FirebaseAppCheck
        public fun getInstance(app: FirebaseApp): FirebaseAppCheck
    }
}
