package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRCollectionReference

@OptIn(ExperimentalForeignApi::class)
actual class CollectionReference(private val iosCollection: FIRCollectionReference) : Query(iosCollection) {
    actual fun document(): DocumentReference {
        return DocumentReference(iosCollection.documentWithAutoID())
    }

    actual fun document(documentPath: String): DocumentReference {
        return DocumentReference(iosCollection.documentWithPath(documentPath))
    }
}
