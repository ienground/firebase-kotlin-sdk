package zone.ien.firebase.auth

public expect object EmailAuthProvider {
    public fun getCredential(email: String, password: String): AuthCredential
}

public fun EmailAuthProvider.credential(email: String, password: String): AuthCredential =
    getCredential(email, password)
