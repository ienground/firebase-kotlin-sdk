package zone.ien.firebase.storage

import com.google.firebase.storage.StorageReference as AndroidStorageReference

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

    actual suspend fun putBytes(data: ByteArray) {
        androidReference.putBytes(data).await()
    }
}
