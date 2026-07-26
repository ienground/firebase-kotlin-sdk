package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow

expect class DocumentReference {
    fun getId(): String
    fun getPath(): String
    suspend fun set(data: Map<String, Any>)
    suspend fun update(data: Map<String, Any>)
    suspend fun delete()
    suspend fun get(): DocumentSnapshot
    suspend fun get(source: Source): DocumentSnapshot
    fun snapshots(): Flow<DocumentSnapshot?>
    fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<DocumentSnapshot?>
}
