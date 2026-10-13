package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFieldPath
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRWriteBatch

@OptIn(ExperimentalForeignApi::class)
actual class WriteBatch(private val iosBatch: FIRWriteBatch) {
    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch {
        iosBatch.setData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): WriteBatch {
        iosBatch.setData(data.toIosData(), documentRef.iosDocument, merge)
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFields: List<String>): WriteBatch {
        iosBatch.setData(data.toIosData(), documentRef.iosDocument, mergeFields = mergeFields)
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>): WriteBatch {
        val paths = mergeFieldPaths.map { it.nativePath() as FIRFieldPath }
        iosBatch.setData(data.toIosData(), documentRef.iosDocument, mergeFields = paths)
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch {
        iosBatch.updateData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun update(documentRef: DocumentReference, field: String, value: Any?, vararg moreFieldsAndValues: Any?): WriteBatch {
        val data = buildUpdateData(field, value, *moreFieldsAndValues)
        iosBatch.updateData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun update(documentRef: DocumentReference, field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?): WriteBatch {
        val data = buildUpdateData(field.nativePath().toString(), value, *moreFieldsAndValues)
        iosBatch.updateData(data.toIosData(), documentRef.iosDocument)
        return this
    }

    actual fun delete(documentRef: DocumentReference): WriteBatch {
        iosBatch.deleteDocument(documentRef.iosDocument)
        return this
    }

    actual suspend fun commit() = suspendCancellableCoroutine<Unit> { cont ->
        iosBatch.commitWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }
}
