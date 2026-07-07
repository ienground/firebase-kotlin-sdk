package zone.ien.firebase.auth

import com.google.firebase.auth.EmailAuthProvider as AndroidEmailAuthProvider

public actual object EmailAuthProvider {
    public actual fun getCredential(email: String, password: String): AuthCredential {
        return AuthCredential(AndroidEmailAuthProvider.getCredential(email, password))
    }
}
