package zone.ien.firebase.remoteconfig

public expect class FirebaseRemoteConfigInfo {
    public val fetchTimeMillis: Long
    public val lastFetchStatus: FetchStatus
    public val configSettings: FirebaseRemoteConfigSettings
}
