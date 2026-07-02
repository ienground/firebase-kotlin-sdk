package zone.ien.firebase.auth

public expect object GoogleAuthProvider {
    public fun getCredential(idToken: String?, accessToken: String?): AuthCredential
}
