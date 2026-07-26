package zone.ien.firebase.firestore

expect class DocumentSnapshot {
    fun getId(): String
    fun getExists(): Boolean
    fun getData(): Map<String, Any>?
    fun get(field: String): Any?
    fun get(field: FieldPath): Any?
    fun getMetadata(): SnapshotMetadata
    internal fun nativeSnapshot(): Any
}
