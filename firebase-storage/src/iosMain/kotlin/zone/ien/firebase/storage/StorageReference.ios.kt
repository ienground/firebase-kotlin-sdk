package zone.ien.firebase.storage

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.create
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = usePinned { pinned ->
    NSData.create(bytes = pinned.addressOf(0), length = this.size.toULong())
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

    actual suspend fun putBytes(data: ByteArray): Unit = suspendCancellableCoroutine { cont ->
        val nsData = data.toNSData()
        iosReference.putData(nsData, metadata = null) { _, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }
}
