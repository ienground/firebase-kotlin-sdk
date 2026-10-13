package zone.ien.firebase.auth

public expect object GithubAuthProvider {
    public fun getCredential(token: String): AuthCredential
}
