package zone.ien.firebase.firestore

import com.google.firebase.firestore.DocumentChange as AndroidDocumentChange
import com.google.firebase.firestore.QuerySnapshot as AndroidQuerySnapshot

actual class QuerySnapshot(private val androidSnapshot: AndroidQuerySnapshot) {
    actual fun getDocuments(): List<DocumentSnapshot> {
        return androidSnapshot.documents.map { DocumentSnapshot(it) }
    }

    actual fun getDocumentChanges(): List<DocumentChange> {
        return androidSnapshot.documentChanges.map { change ->
            val type = when (change.type) {
                AndroidDocumentChange.Type.ADDED -> DocumentChangeType.ADDED
                AndroidDocumentChange.Type.MODIFIED -> DocumentChangeType.MODIFIED
                AndroidDocumentChange.Type.REMOVED -> DocumentChangeType.REMOVED
            }
            DocumentChange(
                document = DocumentSnapshot(change.document),
                type = type,
                oldIndex = change.oldIndex,
                newIndex = change.newIndex
            )
        }
    }

    actual fun getMetadata(): SnapshotMetadata = androidSnapshot.metadata.toCommonMetadata()
}
