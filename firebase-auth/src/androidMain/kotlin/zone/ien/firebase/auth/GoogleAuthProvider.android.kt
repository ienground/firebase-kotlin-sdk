package zone.ien.firebase.auth

import com.google.firebase.auth.GoogleAuthProvider as AndroidGoogleAuthProvider

public actual object GoogleAuthProvider {
    public actual fun getCredential(idToken: String?, accessToken: String?): AuthCredential {
        return AuthCredential(AndroidGoogleAuthProvider.getCredential(idToken, accessToken))
    }
}
