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

    actual fun child(path: String): StorageReference {
        return StorageReference(androidReference.child(path))
    }

    actual suspend fun getDownloadUrl(): String {
        return androidReference.downloadUrl.await().toString()
    }

    actual suspend fun delete() {
        androidReference.delete().await()
    }

    actual fun putBytes(data: ByteArray, metadata: StorageMetadata?): UploadTask {
        val androidTask = if (metadata != null) {
            androidReference.putBytes(data, metadata.toAndroid())
        } else {
            androidReference.putBytes(data)
        }
        return UploadTask(androidTask)
    }

    actual fun putData(data: Data, metadata: StorageMetadata?): UploadTask {
        return putBytes(data.rawData, metadata)
    }

    actual fun putFile(filePath: String, metadata: StorageMetadata?): UploadTask {
        val uri = Uri.fromFile(File(filePath))
        val androidTask = if (metadata != null) {
            androidReference.putFile(uri, metadata.toAndroid())
        } else {
            androidReference.putFile(uri)
        }
        return UploadTask(androidTask)
    }

    actual fun putFile(file: zone.ien.firebase.storage.File, metadata: StorageMetadata?): UploadTask {
        val androidTask = if (metadata != null) {
            androidReference.putFile(file.uri, metadata.toAndroid())
        } else {
            androidReference.putFile(file.uri)
        }
        return UploadTask(androidTask)
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
