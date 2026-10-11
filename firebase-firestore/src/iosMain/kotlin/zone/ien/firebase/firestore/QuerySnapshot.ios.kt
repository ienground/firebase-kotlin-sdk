package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentChange
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentChangeType
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentSnapshot
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRQuerySnapshot

@OptIn(ExperimentalForeignApi::class)
actual class QuerySnapshot(private val iosSnapshot: FIRQuerySnapshot) {
    actual val documents: List<DocumentSnapshot>
        get() = getDocuments()

    actual val documentChanges: List<DocumentChange>
        get() = getDocumentChanges()

    actual val metadata: SnapshotMetadata
        get() = getMetadata()

    actual fun getDocuments(): List<DocumentSnapshot> {
        val nativeDocs = iosSnapshot.documents as List<FIRDocumentSnapshot>
        return nativeDocs.map { DocumentSnapshot(it) }
    }

    actual fun getDocumentChanges(): List<DocumentChange> {
        val nativeChanges = iosSnapshot.documentChanges as List<FIRDocumentChange>
        return nativeChanges.map { change ->
            val type = when (change.type) {
                FIRDocumentChangeType.FIRDocumentChangeTypeAdded -> DocumentChangeType.ADDED
                FIRDocumentChangeType.FIRDocumentChangeTypeModified -> DocumentChangeType.MODIFIED
                FIRDocumentChangeType.FIRDocumentChangeTypeRemoved -> DocumentChangeType.REMOVED
                else -> DocumentChangeType.MODIFIED
            }
            DocumentChange(
                document = DocumentSnapshot(change.document),
                type = type,
                oldIndex = change.oldIndex.toInt(),
                newIndex = change.newIndex.toInt()
            )
        }
    }

    actual fun getMetadata(): SnapshotMetadata = iosSnapshot.metadata.toCommonMetadata()
}
