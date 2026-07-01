package zone.ien.firebase.appcheck

public expect class AppCheckTokenResult {
    private constructor()
    public val token: String
    public val error: Exception?
}
