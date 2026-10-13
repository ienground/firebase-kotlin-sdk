package zone.ien.firebase.auth

import com.google.firebase.auth.AuthCredential as AndroidAuthCredential

public actual class AuthCredential private actual constructor() {
    internal lateinit var androidCredential: AndroidAuthCredential

    internal constructor(androidCredential: AndroidAuthCredential) : this() {
        this.androidCredential = androidCredential
    }

    public actual val provider: String
        get() = androidCredential.provider
}
