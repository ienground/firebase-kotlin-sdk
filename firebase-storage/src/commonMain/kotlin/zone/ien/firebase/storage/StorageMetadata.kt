package zone.ien.firebase.storage

public class StorageMetadata(
    public val bucket: String? = null,
    public val cacheControl: String? = null,
    public val contentDisposition: String? = null,
    public val contentEncoding: String? = null,
    public val contentLanguage: String? = null,
    public val contentType: String? = null,
    public val customMetadata: Map<String, String>? = null,
    public val md5Hash: String? = null,
    public val generation: Long = 0L,
    public val metageneration: Long = 0L,
    public val name: String? = null,
    public val path: String? = null,
    public val sizeBytes: Long = 0L,
    public val creationTimeMillis: Long = 0L,
    public val updatedTimeMillis: Long = 0L,
) {
    public class Builder {
        public var cacheControl: String? = null
        public var contentDisposition: String? = null
        public var contentEncoding: String? = null
        public var contentLanguage: String? = null
        public var contentType: String? = null
        public var customMetadata: Map<String, String>? = null

        public fun setCustomMetadata(key: String, value: String?): Builder {
            val current = customMetadata?.toMutableMap() ?: mutableMapOf()
            if (value != null) {
                current[key] = value
            } else {
                current.remove(key)
            }
            customMetadata = current
            return this
        }

        public fun build(): StorageMetadata = StorageMetadata(
            cacheControl = cacheControl,
            contentDisposition = contentDisposition,
            contentEncoding = contentEncoding,
            contentLanguage = contentLanguage,
            contentType = contentType,
            customMetadata = customMetadata
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StorageMetadata) return false

        if (bucket != other.bucket) return false
        if (cacheControl != other.cacheControl) return false
        if (contentDisposition != other.contentDisposition) return false
        if (contentEncoding != other.contentEncoding) return false
        if (contentLanguage != other.contentLanguage) return false
        if (contentType != other.contentType) return false
        if (customMetadata != other.customMetadata) return false
        if (md5Hash != other.md5Hash) return false
        if (generation != other.generation) return false
        if (metageneration != other.metageneration) return false
        if (name != other.name) return false
        if (path != other.path) return false
        if (sizeBytes != other.sizeBytes) return false
        if (creationTimeMillis != other.creationTimeMillis) return false
        if (updatedTimeMillis != other.updatedTimeMillis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bucket?.hashCode() ?: 0
        result = 31 * result + (cacheControl?.hashCode() ?: 0)
        result = 31 * result + (contentDisposition?.hashCode() ?: 0)
        result = 31 * result + (contentEncoding?.hashCode() ?: 0)
        result = 31 * result + (contentLanguage?.hashCode() ?: 0)
        result = 31 * result + (contentType?.hashCode() ?: 0)
        result = 31 * result + (customMetadata?.hashCode() ?: 0)
        result = 31 * result + (md5Hash?.hashCode() ?: 0)
        result = 31 * result + generation.hashCode()
        result = 31 * result + metageneration.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (path?.hashCode() ?: 0)
        result = 31 * result + sizeBytes.hashCode()
        result = 31 * result + creationTimeMillis.hashCode()
        result = 31 * result + updatedTimeMillis.hashCode()
        return result
    }
}

public typealias FirebaseStorageMetadata = StorageMetadata

public fun storageMetadata(builder: StorageMetadata.Builder.() -> Unit): StorageMetadata {
    return StorageMetadata.Builder().apply(builder).build()
}
