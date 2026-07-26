package zone.ien.firebase.firestore

expect class QuerySnapshot {
    fun getDocuments(): List<DocumentSnapshot>
    fun getDocumentChanges(): List<DocumentChange>
    fun getMetadata(): SnapshotMetadata
}

val QuerySnapshot.documents: List<DocumentSnapshot>
    get() = getDocuments()

val QuerySnapshot.documentChanges: List<DocumentChange>
    get() = getDocumentChanges()

val QuerySnapshot.metadata: SnapshotMetadata
    get() = getMetadata()
