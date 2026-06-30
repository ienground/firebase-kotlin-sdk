package zone.ien.firebase.firestore

import com.google.firebase.firestore.DocumentSnapshot as AndroidDocumentSnapshot

actual class DocumentSnapshot(private val androidSnapshot: AndroidDocumentSnapshot) {
    actual fun getId(): String = androidSnapshot.id
    actual fun getExists(): Boolean = androidSnapshot.exists()
    actual fun getData(): Map<String, Any>? = androidSnapshot.data
    actual fun get(field: String): Any? = androidSnapshot.get(field)
}
