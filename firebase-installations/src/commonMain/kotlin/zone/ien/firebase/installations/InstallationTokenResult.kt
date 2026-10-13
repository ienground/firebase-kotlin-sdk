package zone.ien.firebase.installations

public class InstallationTokenResult(
    public override val token: String,
    /** 토큰 만료 시각의 Unix epoch milliseconds입니다. */
    public override val tokenExpirationTimestamp: Long,
    /**
     * 토큰 생성 시각의 Unix epoch milliseconds입니다.
     *
     * 공식 SDK가 토큰 생성 시각을 제공하지 않으면 [UNAVAILABLE_TOKEN_CREATION_TIMESTAMP]입니다.
     */
    public val tokenCreationTimestamp: Long
) : zone.ien.firebase.installations.interop.InstallationTokenResult {
    /** [tokenExpirationTimestamp]의 단위를 명시한 별칭입니다. */
    public val tokenExpirationTimestampMillis: Long
        get() = tokenExpirationTimestamp

    /** [tokenCreationTimestamp]의 단위를 명시한 별칭입니다. */
    public val tokenCreationTimestampMillis: Long
        get() = tokenCreationTimestamp

    public val hasTokenCreationTimestamp: Boolean
        get() = tokenCreationTimestamp != UNAVAILABLE_TOKEN_CREATION_TIMESTAMP

    public companion object {
        public const val UNAVAILABLE_TOKEN_CREATION_TIMESTAMP: Long = 0L
    }
}
