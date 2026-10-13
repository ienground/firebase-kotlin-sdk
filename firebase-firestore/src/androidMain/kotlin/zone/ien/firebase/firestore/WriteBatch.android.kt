package zone.ien.firebase.firestore

import com.google.firebase.firestore.WriteBatch as AndroidWriteBatch
import com.google.firebase.firestore.FieldPath as AndroidFieldPath
import com.google.firebase.firestore.SetOptions as AndroidSetOptions
import kotlin.jvm.JvmName

actual class WriteBatch(private val androidBatch: AndroidWriteBatch) {
    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch {
        androidBatch.set(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): WriteBatch {
        if (merge) {
            androidBatch.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.merge())
        } else {
            androidBatch.set(documentRef.androidDocument, data.toAndroidData())
        }
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFields: List<String>): WriteBatch {
        androidBatch.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.mergeFields(mergeFields))
        return this
    }

    @JvmName("setMergeFieldPaths")
    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>): WriteBatch {
        val paths = mergeFieldPaths.map { it.nativePath() as AndroidFieldPath }
        androidBatch.set(documentRef.androidDocument, data.toAndroidData(), AndroidSetOptions.mergeFieldPaths(paths))
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch {
        androidBatch.update(documentRef.androidDocument, data.toAndroidData())
        return this
    }

    actual fun update(documentRef: DocumentReference, field: String, value: Any?, vararg moreFieldsAndValues: Any?): WriteBatch {
        val data = buildUpdateData(field, value, *moreFieldsAndValues)
        androidBatch.update(documentRef.androidDocument, data)
        return this
    }

    actual fun update(documentRef: DocumentReference, field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?): WriteBatch {
        val data = buildUpdateData(field.nativePath().toString(), value, *moreFieldsAndValues)
        androidBatch.update(documentRef.androidDocument, data)
        return this
    }

    actual fun delete(documentRef: DocumentReference): WriteBatch {
        androidBatch.delete(documentRef.androidDocument)
        return this
    }

    actual suspend fun commit() {
        androidBatch.commit().await()
    }
}
