package zone.ien.firebase.firestore

import com.google.firebase.firestore.DocumentSnapshot as AndroidDocumentSnapshot

actual class DocumentSnapshot(private val androidSnapshot: AndroidDocumentSnapshot) {
    actual val reference: DocumentReference
        get() = DocumentReference(androidSnapshot.reference)

    actual fun getId(): String = androidSnapshot.id
    actual fun getExists(): Boolean = androidSnapshot.exists()

    actual fun getData(): Map<String, Any?>? {
        val data = androidSnapshot.data ?: return null
        return data.mapValues { (_, value) -> value.toCommonValue() }
    }

    actual fun get(field: String): Any? {
        return androidSnapshot.get(field).toCommonValue()
    }

    actual fun get(field: FieldPath): Any? {
        val fp = field.nativePath() as com.google.firebase.firestore.FieldPath
        return androidSnapshot.get(fp).toCommonValue()
    }

    actual fun getMetadata(): SnapshotMetadata {
        return SnapshotMetadata(androidSnapshot.metadata.hasPendingWrites(), androidSnapshot.metadata.isFromCache)
    }

    internal actual fun nativeSnapshot(): Any = androidSnapshot
}
