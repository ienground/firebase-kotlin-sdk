package zone.ien.firebase.storage

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.data
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData.data()
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = this.size.toULong())
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    val byteArray = ByteArray(size)
    byteArray.usePinned { pinned ->
        platform.posix.memcpy(pinned.addressOf(0), bytes, length)
    }
    return byteArray
}

@OptIn(ExperimentalForeignApi::class)
actual class StorageReference(private val iosReference: FIRStorageReference) {
    actual val name: String
        get() = iosReference.name()

    actual val path: String
        get() = iosReference.fullPath()

    actual val bucket: String
        get() = iosReference.bucket()

    actual val parent: StorageReference?
        get() = iosReference.parent()?.let { StorageReference(it) }

    actual val root: StorageReference
        get() = StorageReference(iosReference.root())

    actual override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StorageReference) return false
        return bucket == other.bucket && path == other.path
    }

    actual override fun hashCode(): Int = (bucket + path).hashCode()

    actual fun child(path: String): StorageReference {
        return StorageReference(iosReference.child(path))
    }

    actual suspend fun getDownloadUrl(): String = suspendCancellableCoroutine { cont ->
        iosReference.downloadURLWithCompletion { url, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (url != null) {
                cont.resume(url.absoluteString() ?: "")
            } else {
                cont.resumeWithException(RuntimeException("Url result is null"))
            }
        }
    }

    actual suspend fun delete(): Unit = suspendCancellableCoroutine { cont ->
        iosReference.deleteWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun putBytes(data: ByteArray, metadata: StorageMetadata?) {
        val nsData = data.toNSData()
        val iosMetadata = metadata?.toIos()
        val iosTask = iosReference.putData(nsData, metadata = iosMetadata) { _, _ -> }
        UploadTask(iosTask).await()
    }

    actual suspend fun putData(data: Data, metadata: StorageMetadata?) {
        val iosMetadata = metadata?.toIos()
        val iosTask = iosReference.putData(data.nsData, metadata = iosMetadata) { _, _ -> }
        UploadTask(iosTask).await()
    }

    actual suspend fun putFile(filePath: String, metadata: StorageMetadata?) {
        val nsUrl = NSURL.fileURLWithPath(filePath)
        val iosMetadata = metadata?.toIos()
        val iosTask = iosReference.putFile(nsUrl, metadata = iosMetadata) { _, _ -> }
        UploadTask(iosTask).await()
    }

    actual suspend fun putFile(file: File, metadata: StorageMetadata?) {
        val iosMetadata = metadata?.toIos()
        val iosTask = iosReference.putFile(file.url, metadata = iosMetadata) { _, _ -> }
        UploadTask(iosTask).await()
    }

    actual suspend fun getData(maxDownloadSizeBytes: Long): ByteArray = suspendCancellableCoroutine { cont ->
        iosReference.dataWithMaxSize(maxDownloadSizeBytes) { nsData, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (nsData != null) {
                cont.resume(nsData.toByteArray())
            } else {
                cont.resumeWithException(RuntimeException("Data result is null"))
            }
        }
    }

    actual suspend fun getBytes(maxDownloadSizeBytes: Long): ByteArray {
        return getData(maxDownloadSizeBytes)
    }

    actual fun getFile(destinationPath: String): DownloadTask {
        val nsUrl = NSURL.fileURLWithPath(destinationPath)
        val iosTask = iosReference.writeToFile(nsUrl) { _, _ -> }
        return DownloadTask(iosTask)
    }

    actual fun getFile(file: File): DownloadTask {
        val iosTask = iosReference.writeToFile(file.url) { _, _ -> }
        return DownloadTask(iosTask)
    }

    actual suspend fun getMetadata(): StorageMetadata = suspendCancellableCoroutine { cont ->
        iosReference.metadataWithCompletion { metadata, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (metadata != null) {
                cont.resume(metadata.toCommon())
            } else {
                cont.resumeWithException(RuntimeException("Metadata result is null"))
            }
        }
    }

    actual suspend fun updateMetadata(metadata: StorageMetadata): StorageMetadata = suspendCancellableCoroutine { cont ->
        iosReference.updateMetadata(metadata.toIos()) { updatedMetadata, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (updatedMetadata != null) {
                cont.resume(updatedMetadata.toCommon())
            } else {
                cont.resumeWithException(RuntimeException("Updated metadata result is null"))
            }
        }
    }
}
