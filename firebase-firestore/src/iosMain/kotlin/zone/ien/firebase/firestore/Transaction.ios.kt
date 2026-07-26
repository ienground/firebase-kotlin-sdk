package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFieldPath
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRTransaction

@OptIn(ExperimentalForeignApi::class)
actual class Transaction(private val iosTransaction: FIRTransaction) {
    actual fun get(documentRef: DocumentReference): DocumentSnapshot {
        val snapshot = iosTransaction.getDocument(documentRef.iosDocument, null)
            ?: throw RuntimeException("Failed to get document in transaction")
        return DocumentSnapshot(snapshot)
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>): Transaction {
        iosTransaction.setData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): Transaction {
        iosTransaction.setData(data.toIosData(), documentRef.iosDocument, merge)
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFields: List<String>): Transaction {
        iosTransaction.setData(data.toIosData(), documentRef.iosDocument, mergeFields = mergeFields)
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>): Transaction {
        val paths = mergeFieldPaths.map { it.nativePath() as FIRFieldPath }
        iosTransaction.setData(data.toIosData(), documentRef.iosDocument, mergeFields = paths)
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): Transaction {
        iosTransaction.updateData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun update(documentRef: DocumentReference, field: String, value: Any?, vararg moreFieldsAndValues: Any?): Transaction {
        val data = buildUpdateData(field, value, *moreFieldsAndValues)
        iosTransaction.updateData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun update(documentRef: DocumentReference, field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?): Transaction {
        val data = buildUpdateData(field.nativePath().toString(), value, *moreFieldsAndValues)
        iosTransaction.updateData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun delete(documentRef: DocumentReference): Transaction {
        iosTransaction.deleteDocument(documentRef.iosDocument)
        return this
    }
}
