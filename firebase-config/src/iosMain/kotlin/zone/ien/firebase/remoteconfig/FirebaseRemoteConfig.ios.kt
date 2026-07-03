package zone.ien.firebase.remoteconfig

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import swiftPMImport.zone.ien.firebase.firebase.config.*
import zone.ien.firebase.FirebaseApp

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseRemoteConfig(
    internal val iosConfig: FIRRemoteConfig
) {
    public actual suspend fun fetch(): Boolean = suspendCancellableCoroutine { continuation ->
        iosConfig.fetchWithCompletionHandler { status, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else {
                continuation.resume(status == FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusSuccess)
            }
        }
    }

    public actual suspend fun activate(): Boolean = suspendCancellableCoroutine { continuation ->
        iosConfig.activateWithCompletion { changed, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else {
                continuation.resume(changed)
            }
        }
    }

    public actual suspend fun fetchAndActivate(): Boolean = suspendCancellableCoroutine { continuation ->
        iosConfig.fetchAndActivateWithCompletionHandler { status, error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.localizedDescription))
            } else {
                continuation.resume(true)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    public actual suspend fun setDefaults(defaults: Map<String, Any>) {
        iosConfig.setDefaults(defaults as Map<Any?, *>)
    }

    public actual fun getValue(key: String): FirebaseRemoteConfigValue {
        return FirebaseRemoteConfigValue(iosConfig.configValueForKey(key))
    }

    public actual fun getBoolean(key: String): Boolean = iosConfig.configValueForKey(key).boolValue
    public actual fun getString(key: String): String = iosConfig.configValueForKey(key).stringValue ?: ""
    public actual fun getLong(key: String): Long = iosConfig.configValueForKey(key).numberValue.longValue
    public actual fun getDouble(key: String): Double = iosConfig.configValueForKey(key).numberValue.doubleValue

    public actual fun getKeysByPrefix(prefix: String): Set<String> {
        val keys = iosConfig.keysWithPrefix(prefix)
        return keys.map { it.toString() }.toSet()
    }

    public actual fun getInfo(): FirebaseRemoteConfigInfo {
        return FirebaseRemoteConfigInfo(
            fetchTime = iosConfig.lastFetchTime,
            status = iosConfig.lastFetchStatus,
            settings = iosConfig.configSettings
        )
    }

    public actual suspend fun setSettings(settings: FirebaseRemoteConfigSettings) {
        val iosSettings = FIRRemoteConfigSettings()
        iosSettings.minimumFetchInterval = settings.minimumFetchIntervalInSeconds.toDouble()
        iosSettings.fetchTimeout = settings.fetchTimeoutInSeconds.toDouble()
        iosConfig.configSettings = iosSettings
    }

    public actual fun addOnConfigUpdateListener(listener: ConfigUpdateListener): ConfigUpdateListenerRegistration {
        val callback = { update: FIRRemoteConfigUpdate?, error: platform.Foundation.NSError? ->
            if (error != null) {
                listener.onError(Exception(error.localizedDescription))
            } else if (update != null) {
                listener.onUpdate(ConfigUpdate(update))
            }
        }
        val reg: FIRConfigUpdateListenerRegistration = iosConfig.addOnConfigUpdateListener(callback)
        return ConfigUpdateListenerRegistration(reg)
    }

    public actual companion object {
        public actual val instance: FirebaseRemoteConfig
            get() = FirebaseRemoteConfig(FIRRemoteConfig.remoteConfig())

        public actual fun getInstance(app: FirebaseApp): FirebaseRemoteConfig {
            return FirebaseRemoteConfig(FIRRemoteConfig.remoteConfigWithApp(app.iosApp))
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
