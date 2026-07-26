package zone.ien.firebase.storage

import com.google.firebase.storage.StorageMetadata as AndroidStorageMetadata

internal fun AndroidStorageMetadata.toCommon(): StorageMetadata {
    val customMap = customMetadataKeys.associateWith { getCustomMetadata(it) }.filterValues { it != null } as Map<String, String>
    return StorageMetadata(
        bucket = bucket,
        cacheControl = cacheControl,
        contentDisposition = contentDisposition,
        contentEncoding = contentEncoding,
        contentLanguage = contentLanguage,
        contentType = contentType,
        customMetadata = customMap.ifEmpty { null },
        md5Hash = md5Hash,
        generation = generation?.toLongOrNull() ?: 0L,
        metageneration = metadataGeneration?.toLongOrNull() ?: 0L,
        name = name,
        path = path,
        sizeBytes = sizeBytes,
        creationTimeMillis = creationTimeMillis,
        updatedTimeMillis = updatedTimeMillis
    )
}

internal fun StorageMetadata.toAndroid(): AndroidStorageMetadata {
    val builder = AndroidStorageMetadata.Builder()
    cacheControl?.let { builder.cacheControl = it }
    contentDisposition?.let { builder.contentDisposition = it }
    contentEncoding?.let { builder.contentEncoding = it }
    contentLanguage?.let { builder.contentLanguage = it }
    contentType?.let { builder.contentType = it }
    customMetadata?.forEach { (k, v) -> builder.setCustomMetadata(k, v) }
    return builder.build()
}
