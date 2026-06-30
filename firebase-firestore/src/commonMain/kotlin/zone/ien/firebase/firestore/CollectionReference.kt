package zone.ien.firebase.firestore

expect class CollectionReference : Query {
    fun document(): DocumentReference
    fun document(documentPath: String): DocumentReference
}
