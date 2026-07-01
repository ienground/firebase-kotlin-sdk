package zone.ien.firebase.appcheck.interop

import zone.ien.firebase.appcheck.AppCheckTokenResult
import com.google.firebase.appcheck.interop.InteropAppCheckTokenProvider as AndroidInteropAppCheckTokenProvider

public actual interface InteropAppCheckTokenProvider {
    public actual suspend fun getToken(forceRefresh: Boolean): AppCheckTokenResult
    public actual suspend fun getLimitedUseToken(): AppCheckTokenResult
    public actual fun addAppCheckTokenListener(listener: AppCheckTokenListener)
    public actual fun removeAppCheckTokenListener(listener: AppCheckTokenListener)
}

public class AndroidInteropAppCheckTokenProvider(
    private val androidProvider: AndroidInteropAppCheckTokenProvider
) : InteropAppCheckTokenProvider {

    private val listenerMap = mutableMapOf<AppCheckTokenListener, AndroidAppCheckTokenListenerAdapter>()

    override suspend fun getToken(forceRefresh: Boolean): AppCheckTokenResult {
        val res = androidProvider.getToken(forceRefresh).await()
        return AppCheckTokenResult(res)
    }

    override suspend fun getLimitedUseToken(): AppCheckTokenResult {
        val res = androidProvider.limitedUseToken.await()
        return AppCheckTokenResult(res)
    }

    override fun addAppCheckTokenListener(listener: AppCheckTokenListener) {
        val adapter = AndroidAppCheckTokenListenerAdapter(listener)
        listenerMap[listener] = adapter
        androidProvider.addAppCheckTokenListener(adapter)
    }

    override fun removeAppCheckTokenListener(listener: AppCheckTokenListener) {
        val adapter = listenerMap.remove(listener)
        if (adapter != null) {
            androidProvider.removeAppCheckTokenListener(adapter)
        }
    }
}
