package zone.ien.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore as AndroidFirebaseFirestore

actual class FirebaseFirestore(private val androidFirestore: AndroidFirebaseFirestore) {
    actual fun collection(collectionPath: String): CollectionReference {
        return CollectionReference(androidFirestore.collection(collectionPath))
    }

    actual fun document(documentPath: String): DocumentReference {
        return DocumentReference(androidFirestore.document(documentPath))
    }

    actual companion object {
        actual fun getInstance(): FirebaseFirestore {
            return FirebaseFirestore(AndroidFirebaseFirestore.getInstance())
        }
    }
}
