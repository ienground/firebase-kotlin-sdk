package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentSnapshot

@OptIn(ExperimentalForeignApi::class)
actual class DocumentSnapshot(private val iosSnapshot: FIRDocumentSnapshot) {
    actual val id: String
        get() = getId()

    actual val exists: Boolean
        get() = getExists()

    actual val metadata: SnapshotMetadata
        get() = getMetadata()

    actual val reference: DocumentReference
        get() = DocumentReference(iosSnapshot.reference)

    actual fun getId(): String = iosSnapshot.documentID
    actual fun getExists(): Boolean = iosSnapshot.exists

    actual fun getData(): Map<String, Any?>? {
        val data = iosSnapshot.data() ?: return null
        @Suppress("UNCHECKED_CAST")
        return (data as Map<String, Any?>).mapValues { (_, value) -> value.toCommonValue() }
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T> get(field: String): T {
        return iosSnapshot.valueForField(field).toCommonValue() as T
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T> get(field: FieldPath): T {
        return iosSnapshot.valueForField(field.nativePath()).toCommonValue() as T
    }

    actual fun getMetadata(): SnapshotMetadata = iosSnapshot.metadata.toCommonMetadata()

    internal actual fun nativeSnapshot(): Any = iosSnapshot
}
