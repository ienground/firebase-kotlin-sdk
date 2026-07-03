package zone.ien.firebase.installations

public class InstallationTokenResult(
    override public val token: String,
    override public val tokenExpirationTimestamp: Long,
    public val tokenCreationTimestamp: Long
) : zone.ien.firebase.installations.interop.InstallationTokenResult
