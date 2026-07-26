package zone.ien.firebase.installations.interop

public interface InstallationTokenResult {
    public val token: String

    /** 토큰 만료 시각의 Unix epoch milliseconds입니다. */
    public val tokenExpirationTimestamp: Long
}
