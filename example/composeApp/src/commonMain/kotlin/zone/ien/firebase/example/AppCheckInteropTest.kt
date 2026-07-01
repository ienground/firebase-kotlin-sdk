package zone.ien.firebase.example

import zone.ien.firebase.appcheck.AppCheckTokenResult
import zone.ien.firebase.appcheck.interop.AppCheckTokenListener
import zone.ien.firebase.appcheck.interop.InteropAppCheckTokenProvider

public object AppCheckInteropTest {
    public suspend fun verifyInteropContracts(provider: InteropAppCheckTokenProvider) {
        // 1. Verify token fetching flow
        try {
            val result: AppCheckTokenResult = provider.getToken(forceRefresh = false)
            println("Interop token fetched successfully: ${result.token}")
            if (result.error != null) {
                println("Token fetch error metadata: ${result.error?.message}")
            }
        } catch (e: Exception) {
            println("Token fetch compilation check failed: ${e.message}")
        }

        // 2. Verify token listener interface compilation alignment
        val listener = object : AppCheckTokenListener {
            override fun onAppCheckTokenChanged(tokenResult: AppCheckTokenResult) {
                println("Token changed via interop callback: ${tokenResult.token}")
            }
        }

        try {
            provider.addAppCheckTokenListener(listener)
            provider.removeAppCheckTokenListener(listener)
        } catch (e: UnsupportedOperationException) {
            // Expected fallback path on iOS
            println("Listener registration fallback: ${e.message}")
        }
    }
}
