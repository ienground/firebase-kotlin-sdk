package zone.ien.firebase.remoteconfig

public class FirebaseRemoteConfigSettings private constructor(
    public val minimumFetchIntervalInSeconds: Long,
    public val fetchTimeoutInSeconds: Long
) {
    public class Builder {
        private var minimumFetchIntervalInSeconds: Long = 43200L
        private var fetchTimeoutInSeconds: Long = 60L

        public fun setMinimumFetchIntervalInSeconds(minimumFetchIntervalInSeconds: Long): Builder {
            this.minimumFetchIntervalInSeconds = minimumFetchIntervalInSeconds
            return this
        }

        public fun setFetchTimeoutInSeconds(fetchTimeoutInSeconds: Long): Builder {
            this.fetchTimeoutInSeconds = fetchTimeoutInSeconds
            return this
        }

        public fun build(): FirebaseRemoteConfigSettings {
            return FirebaseRemoteConfigSettings(minimumFetchIntervalInSeconds, fetchTimeoutInSeconds)
        }
    }
}
