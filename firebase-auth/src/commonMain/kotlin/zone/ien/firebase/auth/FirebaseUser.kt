package zone.ien.firebase.auth

public expect class FirebaseUser private constructor() {
    public val uid: String
    public val email: String?
    public val isAnonymous: Boolean
    public suspend fun delete()
    public suspend fun getIdToken(forceRefresh: Boolean): String
}
