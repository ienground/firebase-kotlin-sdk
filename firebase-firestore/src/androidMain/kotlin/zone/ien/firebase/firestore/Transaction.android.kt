package zone.ien.firebase.firestore

import com.google.firebase.firestore.Transaction as AndroidTransaction
import com.google.firebase.firestore.FieldPath as AndroidFieldPath
import com.google.firebase.firestore.SetOptions as AndroidSetOptions
import kotlin.jvm.JvmName

actual class Transaction(private val androidTransaction: AndroidTransaction) {
    actual fun get(documentRef: DocumentReference): DocumentSnapshot {
        val androidSnapshot = androidTransaction.get(documentRef.androidDocument)
        return DocumentSnapshot(androidSnapshot)
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>): Transaction {
        androidTransaction.set(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): Transaction {
        if (merge) {
            androidTransaction.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.merge())
        } else {
            androidTransaction.set(documentRef.androidDocument, data.toAndroidData())
        }
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFields: List<String>): Transaction {
        androidTransaction.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.mergeFields(mergeFields))
        return this
    }

    @JvmName("setMergeFieldPaths")
    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>): Transaction {
        val paths = mergeFieldPaths.map { it.nativePath() as AndroidFieldPath }
        androidTransaction.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.mergeFieldPaths(paths))
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): Transaction {
        androidTransaction.update(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun update(documentRef: DocumentReference, field: String, value: Any?, vararg moreFieldsAndValues: Any?): Transaction {
        val data = buildUpdateData(field, value, *moreFieldsAndValues)
        androidTransaction.update(documentRef.androidDocument, data)
        return this
    }

    actual fun update(documentRef: DocumentReference, field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?): Transaction {
        val data = buildUpdateData(field.nativePath().toString(), value, *moreFieldsAndValues)
        androidTransaction.update(documentRef.androidDocument, data)
        return this
    }

    actual fun delete(documentRef: DocumentReference): Transaction {
        androidTransaction.delete(documentRef.androidDocument)
        return this
    }
}
