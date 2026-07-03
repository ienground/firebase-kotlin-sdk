package zone.ien.firebase.installations

public class InstallationTokenResult(
    public val token: String,
    public val tokenExpirationTimestamp: Long,
    public val tokenCreationTimestamp: Long
)
