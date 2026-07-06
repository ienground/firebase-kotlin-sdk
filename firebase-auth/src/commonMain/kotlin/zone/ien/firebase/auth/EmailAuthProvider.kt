package zone.ien.firebase.auth

public expect object EmailAuthProvider {
    public fun getCredential(email: String, password: String): AuthCredential
}
