package zone.ien.firebase.remoteconfig

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.get
import kotlinx.cinterop.readBytes
import swiftPMImport.zone.ien.firebase.firebase.config.FIRRemoteConfigValue
import swiftPMImport.zone.ien.firebase.firebase.config.FIRRemoteConfigSource

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseRemoteConfigValue(
    internal val iosValue: FIRRemoteConfigValue
) {
    public actual fun asBoolean(): Boolean = iosValue.boolValue

    public actual fun asString(): String = iosValue.stringValue

    public actual fun asLong(): Long = iosValue.numberValue.longValue

    public actual fun asDouble(): Double = iosValue.numberValue.doubleValue

    public actual fun asByteArray(): ByteArray {
        val nsData = iosValue.dataValue
        return nsData.bytes?.readBytes(nsData.length.toInt()) ?: ByteArray(0)
    }

    public actual val source: ValueSource
        get() = when (iosValue.source) {
            FIRRemoteConfigSource.FIRRemoteConfigSourceStatic -> ValueSource.STATIC
            FIRRemoteConfigSource.FIRRemoteConfigSourceDefault -> ValueSource.DEFAULT
            FIRRemoteConfigSource.FIRRemoteConfigSourceRemote -> ValueSource.REMOTE
            else -> ValueSource.STATIC
        }
}
