package zone.ien.firebase.storage

import com.google.firebase.storage.FirebaseStorage as AndroidFirebaseStorage

actual class FirebaseStorage(private val androidStorage: AndroidFirebaseStorage) {
    actual val reference: StorageReference
        get() = StorageReference(androidStorage.reference)

    actual fun getReference(path: String): StorageReference {
        return StorageReference(androidStorage.getReference(path))
    }

    actual fun getReferenceFromUrl(url: String): StorageReference {
        return StorageReference(androidStorage.getReferenceFromUrl(url))
    }

    actual fun useEmulator(host: String, port: Int) {
        androidStorage.useEmulator(host, port)
    }

    actual companion object {
        actual fun getInstance(): FirebaseStorage {
            return FirebaseStorage(AndroidFirebaseStorage.getInstance())
        }
    }
}
