package zone.ien.firebase.firestore

import kotlin.jvm.JvmName

expect class WriteBatch {
    fun set(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): WriteBatch
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFields: List<String>): WriteBatch
    @JvmName("setMergeFieldPaths")
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>): WriteBatch
    fun update(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch
    fun update(documentRef: DocumentReference, field: String, value: Any?, vararg moreFieldsAndValues: Any?): WriteBatch
    fun update(documentRef: DocumentReference, field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?): WriteBatch
    fun delete(documentRef: DocumentReference): WriteBatch
    suspend fun commit()
}
