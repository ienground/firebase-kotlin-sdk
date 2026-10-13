package zone.ien.firebase.firestore

enum class DocumentChangeType {
    ADDED,
    MODIFIED,
    REMOVED
}

data class DocumentChange(
    val document: DocumentSnapshot,
    val type: DocumentChangeType,
    val oldIndex: Int,
    val newIndex: Int
)
