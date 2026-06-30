package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow

expect class DocumentReference {
    fun getId(): String
    fun getPath(): String
    suspend fun set(data: Map<String, Any>)
    suspend fun update(data: Map<String, Any>)
    suspend fun delete()
    suspend fun get(): DocumentSnapshot
    fun snapshots(): Flow<DocumentSnapshot?>
}
