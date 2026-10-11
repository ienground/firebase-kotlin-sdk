package zone.ien.firebase.installations.interop

import com.google.firebase.installations.InstallationTokenResult as AndroidTokenResult

public class AndroidInstallationTokenResult(
    private val androidTokenResult: AndroidTokenResult
) : InstallationTokenResult {
    override val token: String
        get() = androidTokenResult.token
    override val tokenExpirationTimestamp: Long
        get() = Math.multiplyExact(
            Math.addExact(
                androidTokenResult.tokenCreationTimestamp,
                androidTokenResult.tokenExpirationTimestamp
            ),
            MILLIS_PER_SECOND
        )
}

private const val MILLIS_PER_SECOND: Long = 1_000L
