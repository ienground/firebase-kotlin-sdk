package zone.ien.firebase.firestore

import com.google.firebase.firestore.CollectionReference as AndroidCollectionReference

actual class CollectionReference(internal val androidCollection: AndroidCollectionReference) : Query(androidCollection) {
    actual val id: String
        get() = androidCollection.id
    actual val path: String
        get() = androidCollection.path
    actual val parent: DocumentReference?
        get() = androidCollection.parent?.let { DocumentReference(it) }

    actual fun document(): DocumentReference = DocumentReference(androidCollection.document())
    actual fun document(documentPath: String): DocumentReference = DocumentReference(androidCollection.document(documentPath))
    actual suspend fun add(data: Map<String, Any?>): DocumentReference =
        DocumentReference(androidCollection.add(data.toAndroidData()).await())
}
