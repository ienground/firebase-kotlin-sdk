package zone.ien.firebase.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue as AndroidValue
import com.google.firebase.remoteconfig.FirebaseRemoteConfig as AndroidRemoteConfig

public actual class FirebaseRemoteConfigValue(
    internal val androidValue: AndroidValue
) {
    public actual fun asBoolean(): Boolean = androidValue.asBoolean()
    public actual fun asString(): String = androidValue.asString()
    public actual fun asLong(): Long = androidValue.asLong()
    public actual fun asDouble(): Double = androidValue.asDouble()
    public actual fun asByteArray(): ByteArray = androidValue.asByteArray()
    public actual val source: ValueSource
        get() = when (androidValue.source) {
            AndroidRemoteConfig.VALUE_SOURCE_STATIC -> ValueSource.STATIC
            AndroidRemoteConfig.VALUE_SOURCE_DEFAULT -> ValueSource.DEFAULT
            AndroidRemoteConfig.VALUE_SOURCE_REMOTE -> ValueSource.REMOTE
            else -> ValueSource.STATIC
        }
}
