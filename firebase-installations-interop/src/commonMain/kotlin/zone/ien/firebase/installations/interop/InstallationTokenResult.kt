package zone.ien.firebase.installations.interop

public interface InstallationTokenResult {
    public val token: String
    public val tokenExpirationTimestamp: Long
}
