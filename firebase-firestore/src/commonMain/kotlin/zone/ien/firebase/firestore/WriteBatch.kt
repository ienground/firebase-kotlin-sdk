package zone.ien.firebase.firestore

expect class WriteBatch {
    fun set(documentRef: DocumentReference, data: Map<String, Any>): WriteBatch
    fun set(documentRef: DocumentReference, data: Map<String, Any>, merge: Boolean): WriteBatch
    fun update(documentRef: DocumentReference, data: Map<String, Any>): WriteBatch
    fun delete(documentRef: DocumentReference): WriteBatch
    suspend fun commit()
}
