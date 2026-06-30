package zone.ien.firebase.firestore

expect class QuerySnapshot {
    fun getDocuments(): List<DocumentSnapshot>
}
