package zone.ien.firebase.transport.cct

import zone.ien.firebase.transport.Encoding

public actual class CCTDestination {
    public actual val name: String
    public actual val extras: ByteArray?
    public actual val apiKey: String?
    public actual val endpoint: String
    public actual val supportedEncodings: Set<Encoding>

    public actual constructor(endpoint: String, apiKey: String?) {
        this.name = "cct"
        this.extras = null
        this.apiKey = apiKey
        this.endpoint = endpoint
        this.supportedEncodings = emptySet()
    }

    internal constructor(name: String, endpoint: String, apiKey: String?) {
        this.name = name
        this.extras = null
        this.apiKey = apiKey
        this.endpoint = endpoint
        this.supportedEncodings = emptySet()
    }

    public actual fun asByteArray(): ByteArray? {
        return null
    }

    public actual companion object {
        public actual val INSTANCE: CCTDestination =
            CCTDestination("cct", "https://firebaselogging-pa.googleapis.com/v1/firebaselogging", null)

        public actual val LEGACY_INSTANCE: CCTDestination =
            CCTDestination("cct", "https://firebaselogging-pa.googleapis.com/v1/firebaselogging", "legacy-api-key")

        public actual fun fromByteArray(bytes: ByteArray): CCTDestination {
            return INSTANCE
        }
    }
}
