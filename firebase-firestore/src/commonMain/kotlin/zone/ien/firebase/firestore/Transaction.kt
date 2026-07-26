package zone.ien.firebase.firestore

expect class Transaction {
    fun get(documentRef: DocumentReference): DocumentSnapshot
    fun set(documentRef: DocumentReference, data: Map<String, Any>): Transaction
    fun set(documentRef: DocumentReference, data: Map<String, Any>, merge: Boolean): Transaction
    fun update(documentRef: DocumentReference, data: Map<String, Any>): Transaction
    fun delete(documentRef: DocumentReference): Transaction
}
