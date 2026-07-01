package zone.ien.firebase.appcheck

public expect class AppCheckTokenResult {
    public val token: String
    public val error: Exception?
}
