package zone.ien.firebase.appcheck

import com.google.firebase.appcheck.AppCheckTokenResult as AndroidAppCheckTokenResult

public actual class AppCheckTokenResult(private val androidTokenResult: AndroidAppCheckTokenResult) {
    public actual val token: String
        get() = androidTokenResult.token
    public actual val error: Exception?
        get() = androidTokenResult.error
}
