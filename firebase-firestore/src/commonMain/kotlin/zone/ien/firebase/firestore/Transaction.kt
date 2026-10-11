package zone.ien.firebase.firestore

import kotlin.jvm.JvmName

expect class Transaction {
    fun get(documentRef: DocumentReference): DocumentSnapshot
    fun set(documentRef: DocumentReference, data: Map<String, Any?>): Transaction
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): Transaction
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFields: List<String>): Transaction
    @JvmName("setMergeFieldPaths")
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, mergeFieldPaths: List<FieldPath>): Transaction
    fun update(documentRef: DocumentReference, data: Map<String, Any?>): Transaction
    fun update(documentRef: DocumentReference, field: String, value: Any?, vararg moreFieldsAndValues: Any?): Transaction
    fun update(documentRef: DocumentReference, field: FieldPath, value: Any?, vararg moreFieldsAndValues: Any?): Transaction
    fun delete(documentRef: DocumentReference): Transaction
}
