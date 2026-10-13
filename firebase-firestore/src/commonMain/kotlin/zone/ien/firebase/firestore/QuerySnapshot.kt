package zone.ien.firebase.firestore

import kotlin.jvm.JvmName

expect class QuerySnapshot {
    @get:JvmName("documents")
    val documents: List<DocumentSnapshot>
    @get:JvmName("documentChanges")
    val documentChanges: List<DocumentChange>
    @get:JvmName("metadata")
    val metadata: SnapshotMetadata

    fun getDocuments(): List<DocumentSnapshot>
    fun getDocumentChanges(): List<DocumentChange>
    fun getMetadata(): SnapshotMetadata
}

val QuerySnapshot.documents: List<DocumentSnapshot>
    get() = documents

val QuerySnapshot.documentChanges: List<DocumentChange>
    get() = documentChanges

val QuerySnapshot.metadata: SnapshotMetadata
    get() = metadata
