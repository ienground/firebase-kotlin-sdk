package zone.ien.firebase.appcheck

import com.google.firebase.appcheck.AppCheckTokenResult as AndroidAppCheckTokenResult

public actual class AppCheckTokenResult private actual constructor() {
    private lateinit var androidTokenResult: AndroidAppCheckTokenResult
    internal constructor(androidTokenResult: AndroidAppCheckTokenResult) : this() {
        this.androidTokenResult = androidTokenResult
    }
    public actual val token: String
        get() = androidTokenResult.token
    public actual val error: Exception?
        get() = androidTokenResult.error
}