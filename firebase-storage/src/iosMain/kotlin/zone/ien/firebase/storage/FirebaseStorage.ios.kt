package zone.ien.firebase.storage

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorage

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseStorage private constructor(private val iosStorage: FIRStorage) {
    actual val reference: StorageReference
        get() = StorageReference(iosStorage.reference())

    actual fun getReference(path: String): StorageReference {
        return StorageReference(iosStorage.referenceWithPath(path))
    }

    actual fun getReferenceFromUrl(url: String): StorageReference {
        return StorageReference(iosStorage.referenceForURL(url))
    }

    actual fun useEmulator(host: String, port: Int) {
        iosStorage.useEmulatorWithHost(host, port.toLong())
    }

    actual companion object {
        actual fun getInstance(): FirebaseStorage {
            return FirebaseStorage(FIRStorage.storage())
        }
    }
}
