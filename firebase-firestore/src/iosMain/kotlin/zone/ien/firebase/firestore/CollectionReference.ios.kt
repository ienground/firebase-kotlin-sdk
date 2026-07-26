package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRCollectionReference
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentReference

@OptIn(ExperimentalForeignApi::class)
actual class CollectionReference(internal val iosCollection: FIRCollectionReference) : Query(iosCollection) {
    actual val id: String
        get() = iosCollection.collectionID
    actual val path: String
        get() = iosCollection.path
    actual val parent: DocumentReference?
        get() = iosCollection.parent?.let { DocumentReference(it) }

    actual fun document(): DocumentReference = DocumentReference(iosCollection.documentWithAutoID())
    actual fun document(documentPath: String): DocumentReference = DocumentReference(iosCollection.documentWithPath(documentPath))
    actual suspend fun add(data: Map<String, Any?>): DocumentReference = suspendCancellableCoroutine { cont ->
        var ref: FIRDocumentReference? = null
        ref = iosCollection.addDocumentWithData(data.toIosData()) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (ref != null) {
                cont.resume(DocumentReference(ref!!))
            } else {
                cont.resumeWithException(RuntimeException("DocumentReference was null"))
            }
        }
    }
}
