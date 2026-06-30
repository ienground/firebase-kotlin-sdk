package zone.ien.firebase.firestore

import com.google.firebase.firestore.QuerySnapshot as AndroidQuerySnapshot

actual class QuerySnapshot(private val androidSnapshot: AndroidQuerySnapshot) {
    actual fun getDocuments(): List<DocumentSnapshot> {
        return androidSnapshot.documents.map { DocumentSnapshot(it) }
    }
}
