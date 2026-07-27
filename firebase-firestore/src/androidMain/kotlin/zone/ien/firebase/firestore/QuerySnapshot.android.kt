package zone.ien.firebase.firestore

import kotlin.jvm.JvmName
import com.google.firebase.firestore.DocumentChange as AndroidDocumentChange
import com.google.firebase.firestore.QuerySnapshot as AndroidQuerySnapshot

actual class QuerySnapshot(private val androidSnapshot: AndroidQuerySnapshot) {
    @get:JvmName("documents")
    actual val documents: List<DocumentSnapshot>
        get() = getDocuments()

    @get:JvmName("documentChanges")
    actual val documentChanges: List<DocumentChange>
        get() = getDocumentChanges()

    @get:JvmName("metadata")
    actual val metadata: SnapshotMetadata
        get() = getMetadata()

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
