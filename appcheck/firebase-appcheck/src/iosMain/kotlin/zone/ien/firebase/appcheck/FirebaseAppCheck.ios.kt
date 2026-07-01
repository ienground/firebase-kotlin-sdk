package zone.ien.firebase.appcheck

import zone.ien.firebase.FirebaseApp
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.FIRAppCheck
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseAppCheck private constructor(private val iosAppCheck: FIRAppCheck) {

    public actual fun installAppCheckProviderFactory(factory: AppCheckProviderFactory) {
        FIRAppCheck.setAppCheckProviderFactory(factory.iosFactory)
    }

    public actual fun setTokenAutoRefreshEnabled(enabled: Boolean) {
        iosAppCheck.setIsTokenAutoRefreshEnabled(enabled)
    }

    public actual suspend fun getToken(forceRefresh: Boolean): AppCheckToken = suspendCoroutine { cont ->
        iosAppCheck.tokenForcingRefresh(forceRefresh) { token, error ->
            if (error != null) {
                cont.resumeWithException(AppCheckException(error.localizedDescription ?: "Unknown error", null))
            } else if (token != null) {
                cont.resume(AppCheckToken(token))
            } else {
                cont.resumeWithException(AppCheckException("Token and error were both null", null))
            }
        }
    }

    public actual suspend fun getLimitedUseToken(): AppCheckToken = suspendCoroutine { cont ->
        iosAppCheck.limitedUseTokenWithCompletion { token, error ->
            if (error != null) {
                cont.resumeWithException(AppCheckException(error.localizedDescription ?: "Unknown error", null))
            } else if (token != null) {
                cont.resume(AppCheckToken(token))
            } else {
                cont.resumeWithException(AppCheckException("Limited use token and error were both null", null))
            }
        }
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseAppCheck {
            return FirebaseAppCheck(FIRAppCheck.appCheck())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseAppCheck {
            return FirebaseAppCheck(FIRAppCheck.appCheckWithApp(app.iosApp)!!)
        }
    }
}
