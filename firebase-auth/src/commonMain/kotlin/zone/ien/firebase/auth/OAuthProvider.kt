package zone.ien.firebase.auth

public expect class OAuthProvider {
    public val providerId: String
    public constructor(providerId: String)
    public fun getCredential(idToken: String?, accessToken: String?): AuthCredential
    public fun getCredential(idToken: String?, rawNonce: String?, accessToken: String?): AuthCredential
}
