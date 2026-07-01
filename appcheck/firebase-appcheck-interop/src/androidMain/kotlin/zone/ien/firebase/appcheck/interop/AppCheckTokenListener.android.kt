package zone.ien.firebase.appcheck.interop

import zone.ien.firebase.appcheck.AppCheckTokenResult
import com.google.firebase.appcheck.interop.AppCheckTokenListener as AndroidAppCheckTokenListener

public actual interface AppCheckTokenListener {
    public actual fun onAppCheckTokenChanged(tokenResult: AppCheckTokenResult)
}

internal class AndroidAppCheckTokenListenerAdapter(
    private val commonListener: AppCheckTokenListener
) : AndroidAppCheckTokenListener {
    override fun onAppCheckTokenChanged(tokenResult: com.google.firebase.appcheck.AppCheckTokenResult) {
        commonListener.onAppCheckTokenChanged(AppCheckTokenResult(tokenResult))
    }
}
