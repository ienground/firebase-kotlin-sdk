package zone.ien.firebase.storage

expect class StorageReference {
    val name: String
    val path: String
    val bucket: String
    val parent: StorageReference?
    val root: StorageReference

    fun child(path: String): StorageReference
    
    suspend fun getDownloadUrl(): String
    suspend fun delete()
    fun putBytes(data: ByteArray): UploadTask
}
