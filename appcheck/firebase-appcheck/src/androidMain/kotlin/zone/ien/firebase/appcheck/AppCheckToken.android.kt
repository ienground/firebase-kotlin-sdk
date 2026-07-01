package zone.ien.firebase.appcheck

import com.google.firebase.appcheck.AppCheckTokenResult as AndroidAppCheckTokenResult

public actual class AppCheckToken(private val androidToken: AndroidAppCheckTokenResult) {
    public actual val token: String
        get() = androidToken.token ?: ""
    public actual val expireTimeMillis: Long
        get() = 0L
}
