package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFirestore

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseFirestore private constructor(private val iosFirestore: FIRFirestore) {
    actual fun collection(collectionPath: String): CollectionReference {
        return CollectionReference(iosFirestore.collectionWithPath(collectionPath))
    }

    actual fun document(documentPath: String): DocumentReference {
        return DocumentReference(iosFirestore.documentWithPath(documentPath))
    }

    actual companion object {
        actual fun getInstance(): FirebaseFirestore {
            return FirebaseFirestore(FIRFirestore.firestore())
        }
    }
}
