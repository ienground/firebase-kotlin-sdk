package zone.ien.firebase.transport.cct

import zone.ien.firebase.transport.Encoding

public expect class CCTDestination : zone.ien.firebase.transport.runtime.Destination {
    override val name: String
    override val extras: ByteArray?
    public val apiKey: String?
    public val endpoint: String
    public val supportedEncodings: Set<Encoding>

    public constructor(endpoint: String, apiKey: String?)

    public fun asByteArray(): ByteArray?

    public companion object {
        public val INSTANCE: CCTDestination
        public val LEGACY_INSTANCE: CCTDestination
        public fun fromByteArray(bytes: ByteArray): CCTDestination
    }
}
