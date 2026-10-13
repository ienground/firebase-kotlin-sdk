package zone.ien.firebase.appcheck.interop

import zone.ien.firebase.appcheck.AppCheckTokenResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.interop.FIRAppCheckInteropProtocol

@OptIn(ExperimentalForeignApi::class)
public actual interface InteropAppCheckTokenProvider {
    public actual suspend fun getToken(forceRefresh: Boolean): AppCheckTokenResult
    public actual suspend fun getLimitedUseToken(): AppCheckTokenResult
    public actual fun addAppCheckTokenListener(listener: AppCheckTokenListener)
    public actual fun removeAppCheckTokenListener(listener: AppCheckTokenListener)
}

@OptIn(ExperimentalForeignApi::class)
public class IosInteropAppCheckTokenProvider(
    private val iosProvider: FIRAppCheckInteropProtocol
) : InteropAppCheckTokenProvider {

    override suspend fun getToken(forceRefresh: Boolean): AppCheckTokenResult = suspendCoroutine { cont ->
        iosProvider.getTokenForcingRefresh(forceRefresh) { res ->
            cont.resume(AppCheckTokenResult(res))
        }
    }

    override suspend fun getLimitedUseToken(): AppCheckTokenResult = suspendCoroutine { cont ->
        iosProvider.getLimitedUseTokenWithCompletion { res ->
            cont.resume(AppCheckTokenResult(res))
        }
    }

    override fun addAppCheckTokenListener(listener: AppCheckTokenListener) {
        throw UnsupportedOperationException("addAppCheckTokenListener is not supported on iOS.")
    }

    override fun removeAppCheckTokenListener(listener: AppCheckTokenListener) {
        throw UnsupportedOperationException("removeAppCheckTokenListener is not supported on iOS.")
    }
}
