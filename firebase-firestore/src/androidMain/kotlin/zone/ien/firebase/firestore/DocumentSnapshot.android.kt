package zone.ien.firebase.firestore

import com.google.firebase.firestore.FieldPath as AndroidFieldPath
import com.google.firebase.firestore.DocumentSnapshot as AndroidDocumentSnapshot

actual class DocumentSnapshot(private val androidSnapshot: AndroidDocumentSnapshot) {
    actual fun getId(): String = androidSnapshot.id
    actual fun getExists(): Boolean = androidSnapshot.exists()
    @Suppress("UNCHECKED_CAST")
    actual fun getData(): Map<String, Any>? =
        androidSnapshot.data?.toCommonValue() as? Map<String, Any>
    actual fun get(field: String): Any? = androidSnapshot.get(field).toCommonValue()
    actual fun get(field: FieldPath): Any? =
        androidSnapshot.get(field.nativePath() as AndroidFieldPath).toCommonValue()
    actual fun getMetadata(): SnapshotMetadata = androidSnapshot.metadata.toCommonMetadata()
    actual internal fun nativeSnapshot(): Any = androidSnapshot
}
