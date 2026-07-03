package zone.ien.firebase.remoteconfig

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.config.FIRRemoteConfigFetchStatus
import swiftPMImport.zone.ien.firebase.firebase.config.FIRRemoteConfigSettings
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

public actual class FirebaseRemoteConfigInfo @OptIn(ExperimentalForeignApi::class) constructor(
    private val fetchTime: NSDate?,
    private val status: FIRRemoteConfigFetchStatus,
    private val settings: FIRRemoteConfigSettings
) {
    public actual val fetchTimeMillis: Long
        get() = fetchTime?.timeIntervalSince1970?.let { (it * 1000).toLong() } ?: 0L

    @OptIn(ExperimentalForeignApi::class)
    public actual val lastFetchStatus: FetchStatus
        get() = when (status) {
            FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusSuccess -> FetchStatus.SUCCESS
            FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusFailure -> FetchStatus.FAILURE
            FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusThrottled -> FetchStatus.THROTTLED
            FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusNoFetchYet -> FetchStatus.NO_FETCH_YET
            else -> FetchStatus.FAILURE
        }

    @OptIn(ExperimentalForeignApi::class)
    public actual val configSettings: FirebaseRemoteConfigSettings
        get() = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(settings.minimumFetchInterval.toLong())
            .setFetchTimeoutInSeconds(settings.fetchTimeout.toLong())
            .build()
}
