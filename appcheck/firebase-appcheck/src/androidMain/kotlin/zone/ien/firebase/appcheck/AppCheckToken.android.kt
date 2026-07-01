package zone.ien.firebase.appcheck

import com.google.firebase.appcheck.AppCheckTokenResult as AndroidAppCheckTokenResult

public actual class AppCheckToken private actual constructor() {
    private var androidToken: AndroidAppCheckTokenResult? = null

    public constructor(androidToken: AndroidAppCheckTokenResult) : this() {
        this.androidToken = androidToken
    }

    public actual val token: String
        get() = androidToken?.token ?: ""
    public actual val expireTimeMillis: Long
        get() = 0L
}
