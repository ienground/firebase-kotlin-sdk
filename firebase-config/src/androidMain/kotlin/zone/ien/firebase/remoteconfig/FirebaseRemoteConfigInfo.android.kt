package zone.ien.firebase.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo as AndroidInfo
import com.google.firebase.remoteconfig.FirebaseRemoteConfig as AndroidFirebaseRemoteConfig

public actual class FirebaseRemoteConfigInfo(
    internal val androidInfo: AndroidInfo
) {
    public actual val fetchTimeMillis: Long
        get() = androidInfo.fetchTimeMillis

    public actual val lastFetchStatus: FetchStatus
        get() = when (androidInfo.lastFetchStatus) {
            AndroidFirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS -> FetchStatus.SUCCESS
            AndroidFirebaseRemoteConfig.LAST_FETCH_STATUS_FAILURE -> FetchStatus.FAILURE
            AndroidFirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED -> FetchStatus.THROTTLED
            AndroidFirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET -> FetchStatus.NO_FETCH_YET
            else -> FetchStatus.FAILURE
        }

    public actual val configSettings: FirebaseRemoteConfigSettings
        get() = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(androidInfo.configSettings.minimumFetchIntervalInSeconds)
            .setFetchTimeoutInSeconds(androidInfo.configSettings.fetchTimeoutInSeconds)
            .build()
}
