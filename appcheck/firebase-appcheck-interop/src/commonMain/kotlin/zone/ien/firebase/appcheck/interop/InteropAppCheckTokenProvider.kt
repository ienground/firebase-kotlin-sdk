package zone.ien.firebase.appcheck.interop

import zone.ien.firebase.appcheck.AppCheckTokenResult

public expect interface InteropAppCheckTokenProvider {
    public suspend fun getToken(forceRefresh: Boolean): AppCheckTokenResult
    public suspend fun getLimitedUseToken(): AppCheckTokenResult
    public fun addAppCheckTokenListener(listener: AppCheckTokenListener)
    public fun removeAppCheckTokenListener(listener: AppCheckTokenListener)
}
