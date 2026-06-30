package zone.ien.firebase.storage

expect class FirebaseStorage {
    val reference: StorageReference
    fun getReference(path: String): StorageReference
    fun getReferenceFromUrl(url: String): StorageReference

    companion object {
        fun getInstance(): FirebaseStorage
    }
}
