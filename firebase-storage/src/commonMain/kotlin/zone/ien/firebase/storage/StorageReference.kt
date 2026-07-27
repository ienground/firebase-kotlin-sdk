package zone.ien.firebase.storage

expect class StorageReference {
    val name: String
    val path: String
    val bucket: String
    val parent: StorageReference?
    val root: StorageReference

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    fun child(path: String): StorageReference
   
    suspend fun getDownloadUrl(): String
    suspend fun delete()

    suspend fun putBytes(data: ByteArray, metadata: StorageMetadata? = null)
    suspend fun putData(data: Data, metadata: StorageMetadata? = null)
    suspend fun putFile(filePath: String, metadata: StorageMetadata? = null)
    suspend fun putFile(file: File, metadata: StorageMetadata? = null)

    suspend fun getData(maxDownloadSizeBytes: Long): ByteArray
    suspend fun getBytes(maxDownloadSizeBytes: Long): ByteArray

    fun getFile(destinationPath: String): DownloadTask
    fun getFile(file: File): DownloadTask

    suspend fun getMetadata(): StorageMetadata
    suspend fun updateMetadata(metadata: StorageMetadata): StorageMetadata
}
