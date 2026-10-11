package zone.ien.firebase.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig as AndroidFirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings as AndroidSettings
import com.google.firebase.remoteconfig.ConfigUpdate as AndroidConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener as AndroidListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException as AndroidException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import zone.ien.firebase.FirebaseApp

public actual class FirebaseRemoteConfig(
    internal val androidConfig: AndroidFirebaseRemoteConfig
) {
    public actual suspend fun fetch(): Boolean {
        androidConfig.fetch().await()
        return true
    }

    public actual suspend fun activate(): Boolean {
        return androidConfig.activate().await()
    }

    public actual suspend fun fetchAndActivate(): Boolean {
        return androidConfig.fetchAndActivate().await()
    }

    public actual suspend fun setDefaults(defaults: Map<String, Any>) {
        androidConfig.setDefaultsAsync(defaults).await()
    }

    public actual fun getValue(key: String): FirebaseRemoteConfigValue {
        return FirebaseRemoteConfigValue(androidConfig.getValue(key))
    }

    public actual fun getBoolean(key: String): Boolean = androidConfig.getBoolean(key)
    public actual fun getString(key: String): String = androidConfig.getString(key)
    public actual fun getLong(key: String): Long = androidConfig.getLong(key)
    public actual fun getDouble(key: String): Double = androidConfig.getDouble(key)

    public actual fun getKeysByPrefix(prefix: String): Set<String> {
        return androidConfig.getKeysByPrefix(prefix)
    }

    public actual fun getInfo(): FirebaseRemoteConfigInfo {
        return FirebaseRemoteConfigInfo(androidConfig.info)
    }

    public actual suspend fun setSettings(settings: FirebaseRemoteConfigSettings) {
        val androidSettings = AndroidSettings.Builder()
            .setMinimumFetchIntervalInSeconds(settings.minimumFetchIntervalInSeconds)
            .setFetchTimeoutInSeconds(settings.fetchTimeoutInSeconds)
            .build()
        androidConfig.setConfigSettingsAsync(androidSettings).await()
    }

    public actual fun addOnConfigUpdateListener(listener: ConfigUpdateListener): ConfigUpdateListenerRegistration {
        val androidListener = object : AndroidListener {
            override fun onUpdate(configUpdate: AndroidConfigUpdate) {
                listener.onUpdate(ConfigUpdate(configUpdate))
            }

            override fun onError(error: AndroidException) {
                listener.onError(error)
            }
        }
        val reg = androidConfig.addOnConfigUpdateListener(androidListener)
        return ConfigUpdateListenerRegistration(reg)
    }

    public actual companion object {
        public actual val instance: FirebaseRemoteConfig
            get() = FirebaseRemoteConfig(AndroidFirebaseRemoteConfig.getInstance())

        public actual fun getInstance(app: FirebaseApp): FirebaseRemoteConfig {
            return FirebaseRemoteConfig(AndroidFirebaseRemoteConfig.getInstance(app.androidApp))
        }
    }
}

public actual val FirebaseRemoteConfig.configUpdates: Flow<ConfigUpdate>
    get() = callbackFlow {
        val registration = addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                trySendBlocking(configUpdate)
            }

            override fun onError(error: Exception) {
                cancel(message = "Error listening for config updates.", cause = error)
            }
        })
        awaitClose { registration.remove() }
    }
