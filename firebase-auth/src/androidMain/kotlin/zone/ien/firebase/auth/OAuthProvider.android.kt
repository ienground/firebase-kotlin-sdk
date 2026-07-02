package zone.ien.firebase.auth

import com.google.firebase.auth.OAuthProvider as AndroidOAuthProvider

public actual class OAuthProvider actual constructor(public actual val providerId: String) {

    public actual fun getCredential(idToken: String?, accessToken: String?): AuthCredential {
        val builder = AndroidOAuthProvider.newCredentialBuilder(providerId)
        if (idToken != null) builder.setIdToken(idToken)
        if (accessToken != null) builder.setAccessToken(accessToken)
        return AuthCredential(builder.build())
    }

    public actual fun getCredential(idToken: String?, rawNonce: String?, accessToken: String?): AuthCredential {
        val builder = AndroidOAuthProvider.newCredentialBuilder(providerId)
        if (idToken != null) {
            if (rawNonce != null) {
                builder.setIdTokenWithRawNonce(idToken, rawNonce)
            } else {
                builder.setIdToken(idToken)
            }
        }
        if (accessToken != null) builder.setAccessToken(accessToken)
        return AuthCredential(builder.build())
    }
}
