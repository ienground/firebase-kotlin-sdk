package zone.ien.firebase.remoteconfig

import kotlinx.coroutines.flow.Flow
import zone.ien.firebase.FirebaseApp

public expect class FirebaseRemoteConfig {
    public suspend fun fetch(): Boolean
    public suspend fun activate(): Boolean
    public suspend fun fetchAndActivate(): Boolean
    public suspend fun setDefaults(defaults: Map<String, Any>)
    public fun getValue(key: String): FirebaseRemoteConfigValue
    public fun getBoolean(key: String): Boolean
    public fun getString(key: String): String
    public fun getLong(key: String): Long
    public fun getDouble(key: String): Double
    public fun getKeysByPrefix(prefix: String): Set<String>
    public fun getInfo(): FirebaseRemoteConfigInfo
    public suspend fun setSettings(settings: FirebaseRemoteConfigSettings)
    public fun addOnConfigUpdateListener(listener: ConfigUpdateListener): ConfigUpdateListenerRegistration

    public companion object {
        public val instance: FirebaseRemoteConfig
        public fun getInstance(app: FirebaseApp): FirebaseRemoteConfig
    }
}

public expect val FirebaseRemoteConfig.configUpdates: Flow<ConfigUpdate>
