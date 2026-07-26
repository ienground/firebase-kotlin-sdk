package zone.ien.firebase.firestore

import zone.ien.firebase.Firebase

expect class FirebaseFirestore {
    fun collection(collectionPath: String): CollectionReference
    fun document(documentPath: String): DocumentReference

    companion object {
        fun getInstance(): FirebaseFirestore
    }
}

val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore.getInstance()
