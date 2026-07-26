package zone.ien.firebase.storage

import zone.ien.firebase.Firebase

expect class FirebaseStorage {
    val reference: StorageReference
    fun getReference(path: String): StorageReference
    fun getReferenceFromUrl(url: String): StorageReference
    fun useEmulator(host: String, port: Int)

    companion object {
        fun getInstance(): FirebaseStorage
    }
}

val Firebase.storage: FirebaseStorage
    get() = FirebaseStorage.getInstance()
