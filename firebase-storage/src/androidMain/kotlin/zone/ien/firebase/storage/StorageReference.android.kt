package zone.ien.firebase.storage

import android.net.Uri
import java.io.File
import com.google.firebase.storage.StorageReference as AndroidStorageReference
import kotlinx.coroutines.tasks.await

actual class StorageReference(private val androidReference: AndroidStorageReference) {
    actual val name: String
        get() = androidReference.name

    actual val path: String
        get() = androidReference.path

    actual val bucket: String
        get() = androidReference.bucket

    actual val parent: StorageReference?
        get() = androidReference.parent?.let { StorageReference(it) }

    actual val root: StorageReference
        get() = StorageReference(androidReference.root)

    actual override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StorageReference) return false
        return androidReference == other.androidReference
    }

    actual override fun hashCode(): Int = androidReference.hashCode()

    actual fun child(path: String): StorageReference {
        return StorageReference(androidReference.child(path))
    }

    actual suspend fun getDownloadUrl(): String {
        return androidReference.downloadUrl.await().toString()
    }

    actual suspend fun delete() {
        androidReference.delete().await()
    }

    actual suspend fun putBytes(data: ByteArray, metadata: StorageMetadata?) {
        val androidTask = if (metadata != null) {
            androidReference.putBytes(data, metadata.toAndroid())
        } else {
            androidReference.putBytes(data)
        }
        androidTask.await()
    }

    actual suspend fun putData(data: Data, metadata: StorageMetadata?) {
        putBytes(data.rawData, metadata)
    }

    actual suspend fun putFile(filePath: String, metadata: StorageMetadata?) {
        val uri = Uri.fromFile(File(filePath))
        val androidTask = if (metadata != null) {
            androidReference.putFile(uri, metadata.toAndroid())
        } else {
            androidReference.putFile(uri)
        }
        androidTask.await()
    }

    actual suspend fun putFile(file: zone.ien.firebase.storage.File, metadata: StorageMetadata?) {
        val androidTask = if (metadata != null) {
            androidReference.putFile(file.uri, metadata.toAndroid())
        } else {
            androidReference.putFile(file.uri)
        }
        androidTask.await()
    }

    actual suspend fun getData(maxDownloadSizeBytes: Long): ByteArray {
        return androidReference.getBytes(maxDownloadSizeBytes).await()
    }

    actual suspend fun getBytes(maxDownloadSizeBytes: Long): ByteArray {
        return getData(maxDownloadSizeBytes)
    }

    actual fun getFile(destinationPath: String): DownloadTask {
        val file = File(destinationPath)
        return DownloadTask(androidReference.getFile(file))
    }

    actual fun getFile(file: zone.ien.firebase.storage.File): DownloadTask {
        return DownloadTask(androidReference.getFile(file.uri))
    }

    actual suspend fun getMetadata(): StorageMetadata {
        return androidReference.metadata.await().toCommon()
    }

    actual suspend fun updateMetadata(metadata: StorageMetadata): StorageMetadata {
        return androidReference.updateMetadata(metadata.toAndroid()).await().toCommon()
    }
}
