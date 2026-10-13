package zone.ien.firebase.firestore

import kotlin.jvm.JvmName
import com.google.firebase.firestore.DocumentSnapshot as AndroidDocumentSnapshot

actual class DocumentSnapshot(private val androidSnapshot: AndroidDocumentSnapshot) {
    @get:JvmName("id")
    actual val id: String
        get() = getId()

    @get:JvmName("exists")
    actual val exists: Boolean
        get() = getExists()

    @get:JvmName("metadata")
    actual val metadata: SnapshotMetadata
        get() = getMetadata()

    actual val reference: DocumentReference
        get() = DocumentReference(androidSnapshot.reference)

    actual fun getId(): String = androidSnapshot.id
    actual fun getExists(): Boolean = androidSnapshot.exists()

    actual fun getData(): Map<String, Any?>? {
        val data = androidSnapshot.data ?: return null
        return data.mapValues { (_, value) -> value.toCommonValue() }
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T> get(field: String): T {
        return androidSnapshot.get(field).toCommonValue() as T
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T> get(field: FieldPath): T {
        val fp = field.nativePath() as com.google.firebase.firestore.FieldPath
        return androidSnapshot.get(fp).toCommonValue() as T
    }

    actual fun getMetadata(): SnapshotMetadata {
        return SnapshotMetadata(androidSnapshot.metadata.hasPendingWrites(), androidSnapshot.metadata.isFromCache)
    }

    internal actual fun nativeSnapshot(): Any = androidSnapshot
}
