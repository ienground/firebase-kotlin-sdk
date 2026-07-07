package zone.ien.firebase.installations.interop

import com.google.firebase.installations.InstallationTokenResult as AndroidTokenResult

public class AndroidInstallationTokenResult(
    private val androidTokenResult: AndroidTokenResult
) : InstallationTokenResult {
    override val token: String
        get() = androidTokenResult.token
    override val tokenExpirationTimestamp: Long
        get() = androidTokenResult.tokenExpirationTimestamp
}
