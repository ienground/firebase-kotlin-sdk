package zone.ien.firebase.example

import zone.ien.firebase.appcheck.FirebaseAppCheck
import zone.ien.firebase.appcheck.AppCheckException

public object AppCheckTest {
    public suspend fun verifyAppCheckFlow() {
        try {
            // Get AppCheck instance
            val appCheck = FirebaseAppCheck.getInstance()
            
            // Enable auto-refresh token configuration
            appCheck.setTokenAutoRefreshEnabled(true)
            
            // Try fetching debug/production App Check token
            val appCheckToken = appCheck.getToken(forceRefresh = false)
            println("AppCheck Token successfully fetched: ${appCheckToken.token}")
            println("AppCheck Token expire time: ${appCheckToken.expireTimeMillis}")
            
            // Try fetching limited use token
            val limitedUseToken = appCheck.getLimitedUseToken()
            println("AppCheck Limited Use Token successfully fetched: ${limitedUseToken.token}")
            
        } catch (e: AppCheckException) {
            println("AppCheck token fetch error: ${e.message}")
        }
    }
}
