package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentSnapshot

@OptIn(ExperimentalForeignApi::class)
actual class DocumentSnapshot(private val iosSnapshot: FIRDocumentSnapshot) {
    actual fun getId(): String = iosSnapshot.documentID
    actual fun getExists(): Boolean = iosSnapshot.exists
    actual fun getData(): Map<String, Any>? = iosSnapshot.data() as? Map<String, Any>
    actual fun get(field: String): Any? = iosSnapshot.valueForField(field)
}
