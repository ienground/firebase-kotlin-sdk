package zone.ien.firebase.firestore

expect class DocumentSnapshot {
    val reference: DocumentReference
    fun getId(): String
    fun getExists(): Boolean
    fun getData(): Map<String, Any?>?
    fun get(field: String): Any?
    fun get(field: FieldPath): Any?
    fun getMetadata(): SnapshotMetadata
    internal fun nativeSnapshot(): Any
}

val DocumentSnapshot.metadata: SnapshotMetadata
    get() = getMetadata()
