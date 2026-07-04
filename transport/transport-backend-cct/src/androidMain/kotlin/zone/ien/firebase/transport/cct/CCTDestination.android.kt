package zone.ien.firebase.transport.cct

import zone.ien.firebase.transport.Encoding

public actual class CCTDestination internal constructor(
    internal val androidDestination: com.google.android.datatransport.cct.CCTDestination
) {
    public actual val name: String
        get() = androidDestination.name

    public actual val extras: ByteArray?
        get() = androidDestination.extras

    public actual val apiKey: String?
        get() = androidDestination.apiKey

    public actual val endpoint: String
        get() = androidDestination.endPoint

    public actual val supportedEncodings: Set<Encoding>
        get() = androidDestination.supportedEncodings.map { Encoding(it) }.toSet()

    public actual constructor(endpoint: String, apiKey: String?) : this(
        com.google.android.datatransport.cct.CCTDestination(endpoint, apiKey)
    )

    public actual fun asByteArray(): ByteArray? {
        return androidDestination.asByteArray()
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
