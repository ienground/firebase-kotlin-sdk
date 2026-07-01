package zone.ien.firebase.appcheck

public expect class AppCheckToken {
    private constructor()
    public val token: String
    public val expireTimeMillis: Long
}
