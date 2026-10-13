package zone.ien.firebase.storage

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.timeIntervalSince1970
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageMetadata

@OptIn(ExperimentalForeignApi::class)
internal fun FIRStorageMetadata.toCommon(): StorageMetadata {
    val customDict = customMetadata() as? Map<String, String>
    val timeCreatedMillis = timeCreated()?.timeIntervalSince1970?.let { (it * 1000).toLong() } ?: 0L
    val updatedMillis = updated()?.timeIntervalSince1970?.let { (it * 1000).toLong() } ?: 0L
    return StorageMetadata(
        bucket = bucket(),
        cacheControl = cacheControl(),
        contentDisposition = contentDisposition(),
        contentEncoding = contentEncoding(),
        contentLanguage = contentLanguage(),
        contentType = contentType(),
        customMetadata = customDict?.ifEmpty { null },
        md5Hash = md5Hash(),
        generation = generation(),
        metageneration = metageneration(),
        name = name(),
        path = path(),
        sizeBytes = size(),
        creationTimeMillis = timeCreatedMillis,
        updatedTimeMillis = updatedMillis
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun StorageMetadata.toIos(): FIRStorageMetadata {
    val iosMetadata = FIRStorageMetadata()
    cacheControl?.let { iosMetadata.setCacheControl(it) }
    contentDisposition?.let { iosMetadata.setContentDisposition(it) }
    contentEncoding?.let { iosMetadata.setContentEncoding(it) }
    contentLanguage?.let { iosMetadata.setContentLanguage(it) }
    contentType?.let { iosMetadata.setContentType(it) }
    customMetadata?.let { iosMetadata.setCustomMetadata(it as Map<Any?, *>) }
    return iosMetadata
}
