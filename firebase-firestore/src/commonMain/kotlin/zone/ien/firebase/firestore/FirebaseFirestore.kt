package zone.ien.firebase.firestore

expect class FirebaseFirestore {
    fun collection(collectionPath: String): CollectionReference
    fun document(documentPath: String): DocumentReference

    companion object {
        fun getInstance(): FirebaseFirestore
    }
}
