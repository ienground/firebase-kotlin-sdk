package zone.ien.firebase.appcheck

public expect class AppCheckToken {
    public val token: String
    public val expireTimeMillis: Long
}
