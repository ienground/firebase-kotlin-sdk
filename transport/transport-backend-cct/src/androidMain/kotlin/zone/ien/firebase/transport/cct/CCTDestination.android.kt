package zone.ien.firebase.transport.cct

import zone.ien.firebase.transport.Encoding

public actual class CCTDestination internal constructor(
    private val nativeDestination: com.google.android.datatransport.cct.CCTDestination
) : zone.ien.firebase.transport.runtime.Destination, zone.ien.firebase.transport.runtime.AndroidDestinationProvider {

    override val androidDestination: com.google.android.datatransport.runtime.Destination
        get() = nativeDestination

    public actual override val name: String
        get() = nativeDestination.name

    public actual override val extras: ByteArray?
        get() = nativeDestination.extras

    public actual val apiKey: String?
        get() = nativeDestination.apiKey

    public actual val endpoint: String
        get() = nativeDestination.endPoint

    public actual val supportedEncodings: Set<Encoding> =
        nativeDestination.supportedEncodings.map { Encoding.of(it.name) }.toSet()

    public actual constructor(endpoint: String, apiKey: String?) : this(
        com.google.android.datatransport.cct.CCTDestination(endpoint, apiKey)
    )

    public actual fun asByteArray(): ByteArray? {
        return nativeDestination.asByteArray()
    }

    public actual companion object {
        public actual val INSTANCE: CCTDestination =
            CCTDestination(com.google.android.datatransport.cct.CCTDestination.INSTANCE)

        public actual val LEGACY_INSTANCE: CCTDestination =
            CCTDestination(com.google.android.datatransport.cct.CCTDestination.LEGACY_INSTANCE)

        public actual fun fromByteArray(bytes: ByteArray): CCTDestination {
            val android = com.google.android.datatransport.cct.CCTDestination.fromByteArray(bytes)
            return CCTDestination(android)
        }
    }
}
