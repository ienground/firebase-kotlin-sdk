package zone.ien.firebase.auth

public expect class AuthResult private constructor() {
    public val user: FirebaseUser?
}
