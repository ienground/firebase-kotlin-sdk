package zone.ien.firebase.installations

public class InstallationTokenResult(
    public override val token: String,
    public override val tokenExpirationTimestamp: Long,
    public val tokenCreationTimestamp: Long
) : zone.ien.firebase.installations.interop.InstallationTokenResult
