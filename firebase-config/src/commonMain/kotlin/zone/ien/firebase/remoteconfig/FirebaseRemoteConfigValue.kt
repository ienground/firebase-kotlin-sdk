package zone.ien.firebase.remoteconfig

public expect class FirebaseRemoteConfigValue {
    public fun asBoolean(): Boolean
    public fun asString(): String
    public fun asLong(): Long
    public fun asDouble(): Double
    public fun asByteArray(): ByteArray
    public val source: ValueSource
}
