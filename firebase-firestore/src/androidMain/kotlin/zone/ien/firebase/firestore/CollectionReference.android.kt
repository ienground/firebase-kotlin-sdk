package zone.ien.firebase.firestore

import com.google.firebase.firestore.CollectionReference as AndroidCollectionReference

actual class CollectionReference(private val androidCollection: AndroidCollectionReference) : Query(androidCollection) {
    actual fun document(): DocumentReference {
        return DocumentReference(androidCollection.document())
    }

    actual fun document(documentPath: String): DocumentReference {
        return DocumentReference(androidCollection.document(documentPath))
    }
}
