package zone.ien.firebase.storage

expect class FirebaseStorage {
    val reference: StorageReference
    fun getReference(path: String): StorageReference
    fun getReferenceFromUrl(url: String): StorageReference
    fun useEmulator(host: String, port: Int)

    companion object {
        fun getInstance(): FirebaseStorage
    }
}
