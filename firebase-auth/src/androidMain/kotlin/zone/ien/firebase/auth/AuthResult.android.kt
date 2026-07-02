package zone.ien.firebase.auth

import com.google.firebase.auth.AuthResult as AndroidAuthResult

public actual class AuthResult private actual constructor() {
    private var androidResult: AndroidAuthResult? = null

    public constructor(androidResult: AndroidAuthResult) : this() {
        this.androidResult = androidResult
    }

    public actual val user: FirebaseUser?
        get() = androidResult?.user?.let { FirebaseUser(it) }
}
