package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRQuerySnapshot
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentSnapshot

@OptIn(ExperimentalForeignApi::class)
actual class QuerySnapshot(private val iosSnapshot: FIRQuerySnapshot) {
    actual fun getDocuments(): List<DocumentSnapshot> {
        val nativeDocs = iosSnapshot.documents() as List<FIRDocumentSnapshot>
        return nativeDocs.map { DocumentSnapshot(it) }
    }
}
