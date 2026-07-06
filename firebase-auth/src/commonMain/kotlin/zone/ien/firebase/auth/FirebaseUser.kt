package zone.ien.firebase.auth

public expect class FirebaseUser private constructor() {
    public val uid: String
    public val email: String?
    public val isAnonymous: Boolean
    public suspend fun delete()
    public suspend fun getIdToken(forceRefresh: Boolean): String
    public suspend fun unlink(provider: String): FirebaseUser
    public suspend fun sendEmailVerification()
    public suspend fun link(credential: AuthCredential): AuthResult
    public suspend fun updateEmail(email: String)
    public suspend fun updatePassword(password: String)
    public suspend fun reauthenticate(credential: AuthCredential)
    public suspend fun sendEmailVerification()
    public suspend fun updateProfile(request: UserProfileChangeRequest)
    public suspend fun link(credential: AuthCredential): AuthResult
    public suspend fun updateEmail(email: String)
    public suspend fun updatePassword(password: String)
}
