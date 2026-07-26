package zone.ien.firebase.firestore

expect class CollectionReference : Query {
    val id: String
    val path: String
    val parent: DocumentReference?

    fun document(): DocumentReference
    fun document(documentPath: String): DocumentReference
    suspend fun add(data: Map<String, Any?>): DocumentReference
}

import kotlinx.coroutines.flow.Flow

fun CollectionReference.getSnapshots(cache: Boolean = true): Flow<QuerySnapshot> =
    (this as Query).getSnapshots(cache)
